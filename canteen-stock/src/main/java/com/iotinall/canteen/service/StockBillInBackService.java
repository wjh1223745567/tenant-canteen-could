package com.iotinall.canteen.service;

import com.iotinall.canteen.annotations.FreshenMess;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.dto.inbill.StockInBackAcceptanceReq;
import com.iotinall.canteen.dto.inbill.StockInBackApplyReq;
import com.iotinall.canteen.dto.inbill.StockInBackDetailApplyReq;
import com.iotinall.canteen.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 采购退货逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockBillInBackService extends StockBillCommonService {
    /**
     * 创建
     *
     * @author loki
     * @date 2020/08/27 11:51
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void create(StockInBackApplyReq req) {
        //当前任务
        StockFlwTask firstTask = getFirstTask(Constants.BILL_TYPE.STOCK_IN_BACK);

        //下一任务
        StockFlwTask nextTask = getNextTask(firstTask);

        StockBill bill;
        try {
            bill = (StockBill) initBill(nextTask)
                    .setBillType(Constants.BILL_TYPE.STOCK_IN_BACK)
                    .setBillDate(req.getBillDate())
                    .setBillNo(req.getBillNo())
                    .setSupplier(this.stockSupplierService.getSupplierById(req.getSupplierId()))
                    .setRemark(req.getRemark());
            this.stockBillRepository.save(bill);
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

        //保存退货详情
        StockBillDetail inBillDetail;
        StockBillDetail detail;
        StockDetail stockDetail;
        StockBill inBill;
        int index = 0;
        StockGoods goods;
        if (!CollectionUtils.isEmpty(req.getDetails())) {
            for (StockInBackDetailApplyReq d : req.getDetails()) {
                //商品
                goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                        () -> new BizException("商品不存在")
                );

                if (d.getBackAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BizException("【商品" + goods.getName() + "】退货数量应大于0");
                }

                //获取该商品现有的库存
                inBillDetail = this.stockBillDetailRepository.findById(d.getInDetailId()).orElseThrow(
                        () -> new BizException("申请失败,没有找到入库商品")
                );

                stockDetail = stockDetailRepository.findByBillDetail(inBillDetail);
                if (null == stockDetail || stockDetail.getAmount().compareTo(d.getBackAmount()) < 0) {
                    throw new BizException("商品【" + goods.getName() + "】库存不足");
                }

                detail = new StockBillDetail();
                detail.setBill(bill);
                detail.setGoods(goods);
                detail.setPrice(inBillDetail.getPrice());
                detail.setAmount(d.getBackAmount());
                detail.setRealAmount(d.getBackAmount());
                detail.setStockAmount(d.getStockAmount());
                detail.setBillDetail(inBillDetail);
                detail.setSeq(++index);

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
     * 编辑
     *
     * @author loki
     * @date 2020/08/27 11:51
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void update(StockInBackApplyReq req) {
        //判断单据号是否存在
        StockBill bill = this.getBillByNo(req.getBillNo());

        canEdit(bill);

        //供应商
        bill.setSupplier(this.stockSupplierService.getSupplierById(req.getSupplierId()));
        bill.setBillDate(req.getBillDate());
        bill.setRemark(req.getRemark());
        bill.setVersion(System.currentTimeMillis());
        bill = this.stockBillRepository.save(bill);

        //清空原单据对应的详情
        Set<StockBillDetail> detailListOld = bill.getDetails();

        //保存领用详情
        StockBillDetail detail;
        StockBillDetail inBillDetail;
        StockDetail stockDetail;
        StockGoods goods;
        int index = 0;
        if (!CollectionUtils.isEmpty(req.getDetails())) {
            for (StockInBackDetailApplyReq d : req.getDetails()) {
                //商品
                goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                        () -> new BizException("商品不存在")
                );

                if (d.getBackAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BizException("【商品" + goods.getName() + "】退货数量应大于0");
                }

                //获取该商品现有的库存
                inBillDetail = this.stockBillDetailRepository.findById(d.getInDetailId()).orElseThrow(
                        () -> new BizException("申请失败,没有找到入库商品")
                );

                stockDetail = stockDetailRepository.findByBillDetail(inBillDetail);
                if (null == stockDetail || stockDetail.getAmount().compareTo(d.getBackAmount()) < 0) {
                    throw new BizException("商品【" + goods.getName() + "】库存不足");
                }

                detail = new StockBillDetail();
                detail.setBill(bill);
                detail.setGoods(goods);
                detail.setAmount(d.getBackAmount());
                detail.setRealAmount(d.getBackAmount());
                detail.setStockAmount(d.getStockAmount());
                detail.setPrice(inBillDetail.getPrice());
                detail.setBillDetail(inBillDetail);
                detail.setSeq(++index);

                //已经验收的商品
                detail.setAcceptance(this.getBillDetailAcceptanceStatus(detailListOld, goods.getId()));
                this.stockBillDetailRepository.save(detail);
            }
        }

        stockBillDetailRepository.deleteAll(detailListOld);
    }

    /**
     * 验收
     *
     * @author loki
     * @date 2020/09/01 20:39
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void acceptance(StockInBackAcceptanceReq req) {
        StockBill bill = this.getBillByNo(req.getBillNo());

        checkBillStatus(Constants.BILL_TYPE.STOCK_IN_BACK,bill, req.getVersion());

        StockGoods goods = this.stockGoodsRepository.findById(req.getGoodsId()).orElse(null);
        if (null == goods) {
            throw new BizException("商品不存在");
        }

        StockBillDetail billDetail = this.getStockBillDetail(bill, goods);

        BigDecimal realAmount = req.getRealAmount() == null ? BigDecimal.ZERO : req.getRealAmount();
        if (billDetail.getAmount().compareTo(realAmount) < 0) {
            throw new BizException("商品【" + goods.getName() + "】退货数量不能大于申请退货数量");
        }

        //更新单据明细
        billDetail.setAcceptance(true);
        billDetail.setRealAmount(realAmount);

        if (null != req.getFile()) {
            billDetail.setImgUrl(this.saveAcceptanceImage(req.getFile(), bill, billDetail));
        }
        this.stockBillDetailRepository.save(billDetail);

        //更新库存
        Stock stock = goods.getStock();
        stock.setAmount(stock.getAmount().subtract(realAmount));
        this.stockRepository.save(stock);

        //更新入库商品库存明细
        StockDetail stockDetail = this.stockDetailRepository.findByBillDetail(billDetail.getBillDetail());
        if (null == stockDetail
                || null == stockDetail.getAmount()
                || stockDetail.getAmount().compareTo(realAmount) < 0) {
            throw new BizException("商品【" + goods.getName() + "】库存不足");
        }

        //库存详情变化日志
        BigDecimal after = stockDetail.getAmount().subtract(realAmount);
        this.stockDetailChangeRecordRepository.save(buildChangeRecordSub(
                realAmount,
                stockDetail.getAmount(),
                after,
                0,
                stockDetail,
                billDetail
        ));

        stockDetail.setAmount(after);
        this.stockDetailRepository.save(stockDetail);
    }
}
