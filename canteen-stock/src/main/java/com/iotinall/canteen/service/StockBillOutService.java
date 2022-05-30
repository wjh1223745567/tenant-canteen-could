package com.iotinall.canteen.service;

import com.iotinall.canteen.annotations.FreshenMess;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.dto.outbill.StockOutAcceptanceReq;
import com.iotinall.canteen.dto.outbill.StockOutApplyReq;
import com.iotinall.canteen.dto.outbill.StockOutGoodsDetailReq;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 库存逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockBillOutService extends StockBillCommonService {
    /**
     * 新建
     *
     * @author loki
     * @date 2020/08/27 11:51
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void create(StockOutApplyReq req) {
        //当前任务
        StockFlwTask firstTask = getFirstTask(Constants.BILL_TYPE.STOCK_OUT);

        //获取下一步任务
        StockFlwTask nextTask = getNextTask(firstTask);

        FeignEmployeeDTO applyUser = this.feignEmployeeService.findById(req.getApplyUserId() == null ? SecurityUtils.getUserId() : req.getApplyUserId());
        if (null == applyUser) {
            log.info("申请失败，申请人不存在");
            throw new BizException("申请失败");
        }

        StockBill bill;
        try {
            bill = (StockBill) new StockBill()
                    .setApplyUserId(applyUser.getId())
                    .setApplyUserName(applyUser.getName())
                    .setStatus(this.getNewBillStatus(nextTask))
                    .setVersion(System.currentTimeMillis())
                    .setBillType(Constants.BILL_TYPE.STOCK_OUT)
                    .setBillDate(req.getBillDate())
                    .setBillNo(req.getBillNo())
                    .setTask(nextTask)
                    .setApplyUserOrgId(applyUser.getOrgId())
                    .setApplyUserOrgName(applyUser.getOrgName())
                    .setRemark(req.getRemark());
            this.stockBillRepository.save(bill);
        } catch (Exception ex) {
            throw new BizException("申请失败");
        }

        //申请单据权限
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

        //保存领用详情
        StockBillDetail detail;
        StockBillDetail inBillDetail;
        int seq = 0;
        StockGoods goods;
        for (StockOutGoodsDetailReq d : req.getDetails()) {
            goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                    () -> new BizException("商品不存在")
            );

            if (d.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BizException(String.format("【%s】领用数量必须大于0", goods.getName()));
            }

            detail = new StockBillDetail();
            detail.setBill(bill);
            detail.setStockAmount(d.getStockAmount());
            detail.setAmount(d.getAmount());
            detail.setRealAmount(d.getAmount());
            detail.setSeq(++seq);
            detail.setGoods(goods);
            detail.setPrice(goods.getPrice());

            //选择了入库单直接出库
            if (null != d.getInBillDetailId()) {
                //校验出库申请的数量是否小于等于入库单据商品实际入库数量
                StockGoods finalGoods = goods;
                inBillDetail = this.stockBillDetailRepository.findById(d.getInBillDetailId()).orElseThrow(
                        () -> new BizException("商品【" + finalGoods.getName() + "】未入库")
                );

                if (inBillDetail.getRealAmount().compareTo(d.getAmount()) < 0) {
                    throw new BizException("商品【" + goods.getName() + "】库存数不足");
                }

                //入库详情
                StockDetail stockDetail = this.stockDetailRepository.findByBillDetail(inBillDetail);
                if (stockDetail.getAmount().compareTo(d.getAmount()) < 0) {
                    throw new BizException("商品【" + goods.getName() + "】库存数不足");
                }

                detail.setPrice(inBillDetail.getPrice());
                detail.setBillDetail(inBillDetail);
            } else {
                if (null == goods.getStock() || goods.getStock().getAmount().compareTo(d.getAmount()) < 0) {
                    throw new BizException("商品【" + goods.getName() + "】库存数不足");
                }
            }
            this.stockBillDetailRepository.save(detail);
        }

        //下一个人待办
        StockTodo nextTodo = this.buildTodoList(bill, nextTask);
        this.stockTodoListRepository.save(nextTodo);

        //下一个人待办权限
        this.stockBillAuthorityRepository.saveAll(this.buildBillAuthority(
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
    public void update(StockOutApplyReq req) {
        //单据
        StockBill bill = this.getBillByNo(req.getBillNo());

        canEdit(bill);

        FeignEmployeeDTO applyUser = this.feignEmployeeService.findById(req.getApplyUserId() == null ? SecurityUtils.getUserId() : req.getApplyUserId());
        if (null == applyUser) {
            log.info("申请失败，申请人不存在");
            throw new BizException("申请失败");
        }

        bill.setApplyUserOrgId(applyUser.getOrgId());
        bill.setApplyUserOrgName(applyUser.getOrgName());
        bill.setApplyUserId(applyUser.getId());
        bill.setApplyUserName(applyUser.getName());
        bill.setBillDate(req.getBillDate());
        bill.setRemark(req.getRemark());
        bill.setVersion(System.currentTimeMillis());
        bill = this.stockBillRepository.save(bill);

        //清空原单据对应的详情
        Set<StockBillDetail> detailListOld = bill.getDetails();

        //保存领用详情
        StockBillDetail detail;
        StockBillDetail inBillDetail = null;
        int index = 0;
        StockGoods goods;
        if (!CollectionUtils.isEmpty(req.getDetails())) {
            for (StockOutGoodsDetailReq d : req.getDetails()) {

                if (d.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BizException("领用数量应>0");
                }

                goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                        () -> new BizException("商品不存在")
                );

                if (null != d.getInBillDetailId()) {
                    inBillDetail = stockBillDetailRepository.findById(d.getInBillDetailId()).orElse(null);
                    if (null == inBillDetail) {
                        throw new BizException("", "入库单据不存在商品【" + goods.getName() + "】");
                    }
                }

                detail = new StockBillDetail();
                detail.setBill(bill);
                detail.setStockAmount(d.getStockAmount());
                detail.setRealAmount(d.getAmount());
                detail.setAmount(d.getAmount());
                detail.setPrice(null != inBillDetail ? inBillDetail.getPrice() : goods.getPrice());
                detail.setSeq(++index);
                detail.setGoods(goods);
                detail.setAcceptance(this.getBillDetailAcceptanceStatus(detailListOld, goods.getId()));

                if (null != inBillDetail) {
                    detail.setPrice(inBillDetail.getPrice());
                    detail.setBillDetail(inBillDetail);
                }
                this.stockBillDetailRepository.save(detail);
            }
        }

        this.stockBillDetailRepository.deleteAll(detailListOld);
    }

    /**
     * 验收
     *
     * @author loki
     * @date 2020/09/01 20:39
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void acceptance(StockOutAcceptanceReq req) {
        StockBill bill = this.getBillByNo(req.getBillNo());
        checkBillStatus(Constants.BILL_TYPE.STOCK_OUT, bill, req.getVersion());

        StockGoods goods = this.stockGoodsRepository.findById(req.getGoodsId()).orElseThrow(
                () -> new BizException("商品不存在")
        );

        StockBillDetail billDetail = this.getStockBillDetail(bill, goods);

        BigDecimal realAmount = req.getRealAmount() == null ? BigDecimal.ZERO : req.getRealAmount();
        if (billDetail.getAmount().compareTo(realAmount) < 0) {
            throw new BizException("出库数量不能大于申请数量");
        }

        if (null != billDetail.getAcceptance() && billDetail.getAcceptance()) {
            throw new BizException("该商品已经验收");
        }

        Stock stock = goods.getStock();
        if (null == stock
                || stock.getAmount().compareTo(realAmount) < 0) {
            throw new BizException("商品【" + goods.getName() + "】库存不足");
        }

        //更新单据明细
        billDetail.setAcceptance(true);
        billDetail.setRealAmount(realAmount);

        if (null != req.getFile()) {
            billDetail.setImgUrl(this.saveAcceptanceImage(req.getFile(), bill, billDetail));
        }
        this.stockBillDetailRepository.save(billDetail);

        //更新库存
        stock.setAmount(stock.getAmount().subtract(billDetail.getRealAmount()));
        this.stockRepository.save(stock);

        StockBillDetail selectedInBillDetail = billDetail.getBillDetail();
        BigDecimal left;
        BigDecimal before;
        BigDecimal after = BigDecimal.ZERO;
        if (null != selectedInBillDetail) {
            /**
             * 选择了入库单
             * 商品入库单剩余库存 > 出库数量 扣减入库单库存
             * 商品入库单剩余库存 < 出库数量 扣减入库单库存 扣减其他库存
             */
            StockDetail stockDetail = this.stockDetailRepository.findByBillDetail(selectedInBillDetail);
            if (stockDetail.getAmount().compareTo(realAmount) >= 0) {
                before = stockDetail.getAmount();
                after = stockDetail.getAmount().subtract(realAmount);

                //库存明细变动记录
                this.stockDetailChangeRecordRepository.save(buildChangeRecordSub(
                        realAmount,
                        before,
                        after,
                        0,
                        stockDetail,
                        billDetail
                ));

                stockDetail.setAmount(after);
                this.stockDetailRepository.save(stockDetail);
                return;
            } else {
                before = stockDetail.getAmount();
                left = realAmount.subtract(before);

                //库存明细变动记录
                this.stockDetailChangeRecordRepository.save(buildChangeRecordSub(
                        before,
                        before,
                        after,
                        0,
                        stockDetail,
                        billDetail
                ));

                stockDetail.setAmount(BigDecimal.ZERO);
                this.stockDetailRepository.save(stockDetail);
            }
        } else {
            left = realAmount;
        }

        if (left.compareTo(BigDecimal.ZERO) > 0) {
            //先进先出
            List<StockDetail> stockDetailList = stockDetailRepository.findGoodsLeftStock(goods.getId());
            if (CollectionUtils.isEmpty(stockDetailList)) {
                throw new BizException(String.format("商品【%s】库存不足)", goods.getName()));
            }

            int seq = 1;
            for (StockDetail stockDetail : stockDetailList) {
                if (stockDetail.getAmount().compareTo(left) >= 0) {
                    before = stockDetail.getAmount();
                    after = stockDetail.getAmount().subtract(left);
                    this.stockDetailChangeRecordRepository.save(buildChangeRecordSub(
                            left,
                            before,
                            after,
                            seq,
                            stockDetail,
                            billDetail
                    ));
                    stockDetail.setAmount(after);
                    break;
                } else {
                    this.stockDetailChangeRecordRepository.save(buildChangeRecordSub(
                            stockDetail.getAmount(),
                            stockDetail.getAmount(),
                            BigDecimal.ZERO,
                            seq,
                            stockDetail,
                            billDetail
                    ));

                    left = left.subtract(stockDetail.getAmount());
                    stockDetail.setAmount(BigDecimal.ZERO);
                }
                seq++;
            }

            this.stockDetailRepository.saveAll(stockDetailList);
        }
    }

    /**
     * 获取当前登入用户所有出库单据号
     *
     * @author loki
     * @date 2020/09/07 14:57
     */
    public List<String> getOutBillNoList() {
        List<StockBill> billList = this.stockBillRepository.findByApplyUserIdAndStatusAndBillTypeOrderByCreateTimeDesc(
                SecurityUtils.getUserId(),
                Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH, Constants.BILL_TYPE.STOCK_OUT);
        return billList.stream().map(StockBill::getBillNo).collect(Collectors.toList());
    }
}
