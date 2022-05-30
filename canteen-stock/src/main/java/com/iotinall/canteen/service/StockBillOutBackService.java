package com.iotinall.canteen.service;

import com.iotinall.canteen.annotations.FreshenMess;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.dto.bill.StockBillDetailDTO;
import com.iotinall.canteen.dto.goods.StockGoodsDTO;
import com.iotinall.canteen.dto.outbill.StockOutBackAcceptanceReq;
import com.iotinall.canteen.dto.outbill.StockOutBackApplyReq;
import com.iotinall.canteen.dto.outbill.StockOutBackDetailReq;
import com.iotinall.canteen.dto.outbill.StockOutBackGoodsQueryReq;
import com.iotinall.canteen.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 退库逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockBillOutBackService extends StockBillCommonService {
    /**
     * 创建
     *
     * @author loki
     * @date 2020/08/27 11:51
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void create(StockOutBackApplyReq req) {
        //当前任务
        StockFlwTask firstTask = getFirstTask(Constants.BILL_TYPE.STOCK_OUT_BACK);

        //获取下一步任务
        StockFlwTask nextTask = getNextTask(firstTask);

        //出库单据号
        StockBill outBill = this.getBillByNo(req.getOutBillNo());

        StockBill bill;
        try {
            bill = (StockBill) initBill(nextTask)
                    .setBillType(Constants.BILL_TYPE.STOCK_OUT_BACK)
                    .setBillDate(req.getBillDate())
                    .setBillNo(req.getBillNo())
                    .setOutBill(outBill)
                    .setRemark(req.getRemark());
            bill = this.stockBillRepository.save(bill);
        } catch (Exception ex) {
            throw new BizException("申请失败");
        }

        //申请人单据权限
        this.stockBillAuthorityRepository.saveAll(this.buildBillAuthority(
                bill.getId(),
                null,
                firstTask.getHandlerId()
        ));

        //申请操作日志
        StockBillOperateLog optLog = new StockBillOperateLog()
                .setBill(bill)
                .setOptType(Constants.OPT_TYPE.APPLY)
                .setOptUserId(SecurityUtils.getUserId())
                .setOptUserName(SecurityUtils.getUserName())
                .setTask(firstTask);
        this.stockBillOperateLogRepository.save(optLog);

        //保存退库详情
        StockBillDetail detail;
        StockBillDetail outBillDetail;
        int index = 0;
        if (!CollectionUtils.isEmpty(req.getDetails())) {
            for (StockOutBackDetailReq d : req.getDetails()) {
                if (d.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BizException("退库数量必须大于0");
                }

                outBillDetail = this.stockBillDetailRepository.findById(d.getOutDetailId()).orElseThrow(
                        () -> new BizException("商品领用记录不存在")
                );


                if (outBillDetail.getRealAmount().compareTo(outBillDetail.getRealAmount()) < 0) {
                    throw new BizException("", "商品【" + outBillDetail.getGoods().getName() + "】退库数量不能大于实际领用数量");
                }

                if (!checkOutBackAll(outBillDetail.getRealAmount(), d.getAmount(), d.getOutDetailId())) {
                    throw new BizException("", "商品【" + outBillDetail.getGoods().getName() + "】退库数量不能大于实际领用数量");
                }

                detail = new StockBillDetail();
                detail.setBill(bill);
                detail.setPrice(outBillDetail.getPrice());
                detail.setRealAmount(d.getAmount());
                detail.setAmount(d.getAmount());
                detail.setStockAmount(d.getStockAmount());
                detail.setBillDetail(outBillDetail);
                detail.setSeq(++index);
                detail.setGoods(outBillDetail.getGoods());
                this.stockBillDetailRepository.save(detail);
            }
        }

        //下一个人待办
        StockTodo nextTodo = this.buildTodoList(bill, nextTask);
        this.stockTodoListRepository.save(nextTodo);

        //下一个人待办权限
        stockBillAuthorityRepository.saveAll(this.buildBillAuthority(
                bill.getId(),
                nextTodo.getId(),
                nextTask.getHandlerId())
        );
    }

    /**
     * 领用退库
     *
     * @author loki
     * @date 2020/08/27 11:51
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void update(StockOutBackApplyReq req) {
        //判断单据号是否存在
        StockBill bill = this.getBillByNo(req.getBillNo());

        canEdit(bill);

        StockBill outBill = this.getBillByNo(req.getOutBillNo());

        bill.setBillDate(req.getBillDate());
        bill.setVersion(System.currentTimeMillis());
        bill.setRemark(req.getRemark());
        bill.setOutBill(outBill);
        bill = this.stockBillRepository.save(bill);

        Set<StockBillDetail> detailListOld = bill.getDetails();

        //保存退库详情
        StockBillDetail detail;
        StockBillDetail outBillDetail;
        int index = 0;
        if (!CollectionUtils.isEmpty(req.getDetails())) {
            for (StockOutBackDetailReq d : req.getDetails()) {
                if (d.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BizException("退库数量必须大于0");
                }

                outBillDetail = this.stockBillDetailRepository.findById(d.getOutDetailId()).orElseThrow(
                        () -> new BizException("商品领用记录不存在")
                );

                if (outBillDetail.getRealAmount().compareTo(outBillDetail.getRealAmount()) < 0) {
                    throw new BizException("", "商品【" + outBillDetail.getGoods().getName() + "】退库数量不能大于实际领用数量");
                }

                if (!checkOutBackAll(outBillDetail.getRealAmount(), d.getAmount(), d.getOutDetailId())) {
                    throw new BizException("", "商品【" + outBillDetail.getGoods().getName() + "】退库数量不能大于实际领用数量");
                }

                detail = new StockBillDetail();
                detail.setBill(bill);
                detail.setPrice(outBillDetail.getPrice());
                detail.setRealAmount(d.getAmount());
                detail.setAmount(d.getAmount());
                detail.setStockAmount(d.getStockAmount());
                detail.setBillDetail(outBillDetail);
                detail.setSeq(++index);
                detail.setGoods(outBillDetail.getGoods());

                //已经验收的商品
                detail.setAcceptance(this.getBillDetailAcceptanceStatus(detailListOld, outBillDetail.getGoods().getId()));
                this.stockBillDetailRepository.save(detail);
            }
        }

        stockBillDetailRepository.deleteAll(detailListOld);
    }

    /**
     * 领用退库单个验收
     * 1、更新入库明细表
     * 2、更新库存明细
     *
     * @author loki
     * @date 2020/09/01 20:39
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void acceptance(StockOutBackAcceptanceReq req) {
        StockBill bill = this.getBillByNo(req.getBillNo());

        checkBillStatus(Constants.BILL_TYPE.STOCK_OUT_BACK,bill, req.getVersion());

        StockGoods goods = this.stockGoodsRepository.findById(req.getGoodsId()).orElseThrow(
                () -> new BizException("商品不存在")
        );

        //退库明细
        StockBillDetail billDetail = this.getStockBillDetail(bill, goods);

        BigDecimal realAmount = req.getRealAmount() == null ? BigDecimal.ZERO : req.getRealAmount();

        //领用单据明细
        StockBillDetail outBillDetail = this.stockBillDetailRepository.findByBillAndGoods(bill.getOutBill(), goods);
        if (outBillDetail.getRealAmount().compareTo(realAmount) < 0) {
            throw new BizException("", "商品【" + goods.getName() + "】退库数量不能大于实际领用数量");
        }

        if (!checkOutBackAll(outBillDetail.getRealAmount(), realAmount, outBillDetail.getId())) {
            throw new BizException("", "商品【" + outBillDetail.getGoods().getName() + "】退库数量不能大于实际领用数量");
        }

        /**
         * 退库商品返回规则
         * 1、根据商品出库顺序倒序返回商品数
         */
        List<StockDetailChangeRecord> changeRecordList = this.stockDetailChangeRecordRepository.findByBillDetailOrderBySeqDesc(outBillDetail);
        BigDecimal left = realAmount;
        StockDetail stockDetail;
        BigDecimal before;
        BigDecimal after;
        int seq = 0;
        for (StockDetailChangeRecord outRecord : changeRecordList) {
            stockDetail = outRecord.getStockDetail();
            if (outRecord.getAmount().compareTo(left) >= 0) {
                //领用扣减商品的数量大于退库商品数量
                before = stockDetail.getAmount();
                after = before.add(left);
                stockDetail.setAmount(after);
                this.stockDetailRepository.save(stockDetail);

                //更新库存记录
                this.stockDetailChangeRecordRepository.save(this.buildChangeRecordAdd(
                        left,
                        before,
                        after,
                        ++seq,
                        stockDetail,
                        billDetail
                ));
                break;
            } else {
                before = stockDetail.getAmount();
                after = stockDetail.getAmount().add(outRecord.getAmount());

                stockDetail.setAmount(after);
                this.stockDetailRepository.save(stockDetail);

                //更新库存记录
                this.stockDetailChangeRecordRepository.save(this.buildChangeRecordAdd(
                        outRecord.getAmount(),
                        before,
                        after,
                        ++seq,
                        stockDetail,
                        billDetail
                ));

                //剩余
                left = req.getRealAmount().subtract(outRecord.getAmount());
            }
        }

        //更新单据明细
        billDetail.setAcceptance(true);
        billDetail.setRealAmount(realAmount);

        if (null != req.getFile()) {
            billDetail.setImgUrl(this.saveAcceptanceImage(req.getFile(), bill, billDetail));
        }
        this.stockBillDetailRepository.save(billDetail);

        Stock stock = goods.getStock();
        stock.setAmount(stock.getAmount().add(billDetail.getRealAmount()));
        this.stockRepository.save(stock);
    }

    /**
     * 申请退库选择退库商品列表
     *
     * @author loki
     * @date 2021/6/8 15:12
     **/
    public Object getOutBackGoods(StockOutBackGoodsQueryReq req, Pageable pageable) {
        Specification<StockBillDetail> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("outBillNo", req.getOutBillNo()))
                .where(Criterion.eq("stockGoods.warehouse.id", req.getWarehouseId()))
                .where(Criterion.eq("stockGoods.type.id", req.getGoodsTypeId()))
                .whereByOr(
                        Criterion.like("goods.code", req.getKeyword()),
                        Criterion.like("goods.name", req.getKeyword())
                )
                .build();
        Page<StockBillDetail> result = stockBillDetailRepository.findAll(spec, pageable);

        List<StockBillDetail> detailList = result.getContent();
        List<StockBillDetailDTO> detailDTOList = new ArrayList<>();
        StockBillDetailDTO detailDTO;
        for (StockBillDetail detail : detailList) {
            detailDTO = new StockBillDetailDTO();
            detailDTOList.add(detailDTO);
            BeanUtils.copyProperties(detail, detailDTO);

            if (null != detail.getGoods()) {
                StockGoodsDTO goodsDTO = new StockGoodsDTO();
                BeanUtils.copyProperties(detail.getGoods(), goodsDTO);
                detailDTO.setStockGoods(goodsDTO);
            }
        }
        return PageUtil.toPageDTO(detailDTOList, result);
    }
}
