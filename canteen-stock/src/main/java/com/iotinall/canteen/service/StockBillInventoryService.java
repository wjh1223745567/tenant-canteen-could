package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.stock.StockInventoryApplyGoodsReq;
import com.iotinall.canteen.dto.stock.StockInventoryApplyReq;
import com.iotinall.canteen.dto.stock.StockInventoryAuditReq;
import com.iotinall.canteen.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 库存盘点service
 *
 * @author loki
 * @date 2021/7/23 16:59
 **/
@Service
public class StockBillInventoryService extends StockBillCommonService {

    /**
     * 申请
     *
     * @author loki
     * @date 2021/7/30 14:03
     **/
    @Transactional(rollbackFor = Exception.class)
    public void create(StockInventoryApplyReq req) {
        //当前任务
        StockFlwTask firstTask = getFirstTask(Constants.BILL_TYPE.STOCK_INVENTORY);

        //下一任务
        StockFlwTask nextTask = getNextTask(firstTask);

        //仓库
        StockWarehouse warehouse = this.stockWarehouseRepository.findById(req.getWarehouseId()).orElseThrow(() -> new BizException("仓库不存在"));

        StockBill bill;
        try {
            bill = initBill(nextTask)
                    .setBillType(Constants.BILL_TYPE.STOCK_INVENTORY)
                    .setBillDate(req.getBillDate())
                    .setVersion(nextTask.getVersion())
                    .setBillNo(req.getBillNo())
                    .setWarehouse(warehouse);
            bill.setRemark(req.getRemark());
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

        //保存仓库商品详情
        if (!CollectionUtils.isEmpty(req.getDetails())) {
            StockBillDetail detail;
            for (StockInventoryApplyGoodsReq d : req.getDetails()) {
                StockGoods goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(() -> new BizException("商品不存在"));
                detail = new StockBillDetail();
                detail.setAmount(d.getAmount());
                detail.setStockAmount(d.getStockAmount());
                detail.setBill(bill);
                detail.setGoods(goods);
                this.stockBillDetailRepository.save(detail);
            }
        }

        //下一个人待办
        StockTodo nextTodo = this.buildTodoList(bill, nextTask);
        this.stockTodoListRepository.save(nextTodo);

        //下一个人待办权限
        stockBillAuthorityRepository.saveAll(
                this.buildBillAuthority(
                        bill.getId(),
                        nextTodo.getId(),
                        nextTask.getHandlerId())
        );
    }

    /**
     * 更新
     *
     * @author loki
     * @date 2021/7/30 14:03
     **/
    @Transactional(rollbackFor = Exception.class)
    public void update(StockInventoryApplyReq req) {
        //单据
        StockBill bill = this.getBillByNo(req.getBillNo());

        canEdit(bill);

        //仓库
        StockWarehouse warehouse = this.stockWarehouseRepository.findById(req.getWarehouseId()).orElseThrow(() -> new BizException("仓库不存在"));

        bill.setWarehouse(warehouse);
        bill.setBillDate(req.getBillDate());
        bill.setRemark(req.getRemark());
        bill.setVersion(System.currentTimeMillis());
        bill = this.stockBillRepository.save(bill);

        Set<StockBillDetail> detailListOld = bill.getDetails();

        //保存采购入库详情
        StockBillDetail detail;
        int index = 0;
        StockGoods goods;
        for (StockInventoryApplyGoodsReq d : req.getDetails()) {
            //商品
            if (null == d.getGoodsId()) {
                throw new BizException("请选择商品");
            }

            goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                    () -> new BizException("商品不存在")
            );

            detail = new StockBillDetail();
            detail.setBill(bill);
            detail.setAmount(d.getAmount());
            detail.setStockAmount(d.getStockAmount());
            detail.setGoods(goods);
            detail.setSeq(++index);

            //已经验收的商品
            detail.setAcceptance(this.getBillDetailAcceptanceStatus(detailListOld, goods.getId()));
            this.stockBillDetailRepository.save(detail);
        }

        this.stockBillDetailRepository.deleteAll(detailListOld);
    }

    /**
     * 验收
     *
     * @author loki
     * @date 2021/7/30 14:03
     **/
    @Transactional(rollbackFor = Exception.class)
    public void audit(StockInventoryAuditReq req) {
        StockBill bill = this.stockBillRepository.findByBillNo(req.getBillNo());

        if (null == bill) {
            throw new BizException("单据不存在");
        }

        if (!req.getVersion().equals(bill.getVersion())) {
            throw new BizException("单据已被编辑");
        }

        if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_CANCEL == bill.getStatus()) {
            throw new BizException("单据已被取消");
        }

        if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0 != bill.getStatus()
                && Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1 != bill.getStatus()) {
            throw new BizException("单据已被处理");
        }

        StockFlwTask currentTask = bill.getTask();
        LocalDateTime optTime = LocalDateTime.now();
        if (req.getOptType() == Constants.REFUSE) {
            //生成当前操作的操作日志
            StockBillOperateLog optLog = new StockBillOperateLog()
                    .setBill(bill)
                    .setOptType(Constants.OPT_TYPE.AUDIT)
                    .setOptUserId(SecurityUtils.getUserId())
                    .setOptUserName(SecurityUtils.getUserName())
                    .setTask(bill.getTask());
            optLog.setRemark(req.getRemark());
            optLog.setAuditResult(req.getOptType());
            this.stockBillOperateLogRepository.save(optLog);

            bill.setStatus(Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_REFUSE);
            bill.setUpdateTime(optTime);
            bill.setTask(stockFlwConfigService.getEndTask(Constants.BILL_TYPE.STOCK_INVENTORY));
            bill.setAuditUserId(StringUtils.isNotBlank(bill.getAuditUserId()) ? "," + SecurityUtils.getUserId() : SecurityUtils.getUserId() + "");
            bill.setAuditUserName(StringUtils.isNotBlank(bill.getAuditUserName()) ? "," + SecurityUtils.getUserName() : SecurityUtils.getUserName());
            bill = this.stockBillRepository.save(bill);

            //结束日志
            optLog = new StockBillOperateLog()
                    .setBill(bill)
                    .setOptType(Constants.OPT_TYPE.AUDIT)
                    .setOptUserId(SecurityUtils.getUserId())
                    .setOptUserName(SecurityUtils.getUserName())
                    .setTask(bill.getTask());
            optLog.setRemark(req.getRemark());
            optLog.setAuditResult(req.getOptType());
            this.stockBillOperateLogRepository.save(optLog);

        } else {
            /**
             * 盘点审核同意
             */
            StockFlwTask nextTask = stockFlwConfigService.getNextTask(bill.getBillType(), bill.getTask());
            if (Constants.TASK_DEFINE.END.equals(nextTask.getTaskDefine())) {
                if (!CollectionUtils.isEmpty(bill.getDetails())) {
                    for (StockBillDetail detail : bill.getDetails()) {
                        /**
                         * 盘点审批通过库存更新策略：盘亏，扣除最早入库数  盘盈，增添最新入库数
                         */
                        BigDecimal changeAmount;
                        if (detail.getStockAmount().compareTo(detail.getAmount()) == 0) {
                            continue;
                        } else if (detail.getStockAmount().compareTo(detail.getAmount()) < 0) {
                            //盘盈
                            changeAmount = detail.getAmount().subtract(detail.getStockAmount());

                            //最近该商品的入库单
                            StockDetail stockDetail = stockDetailRepository.findLatestStockInDetail(detail.getGoods().getId());
                            if (null == stockDetail) {
                                throw new BizException("该商品尚未入库，请先入库");
                            }
                            BigDecimal stockAmount = stockDetail.getAmount() == null ? BigDecimal.ZERO : stockDetail.getAmount();
                            stockDetail.setAmount(stockAmount.add(changeAmount));
                            this.stockDetailRepository.save(stockDetail);

                            StockDetailChangeRecord changeRecord = this.buildChangeRecordAdd(changeAmount, stockAmount, stockDetail.getAmount(), 0, stockDetail, detail);
                            this.stockDetailChangeRecordRepository.save(changeRecord);
                        } else if (detail.getStockAmount().compareTo(detail.getAmount()) > 0) {
                            //盘亏
                            changeAmount = detail.getAmount().subtract(detail.getStockAmount());

                            //获取库存数>0的库存明细 库存不足以扣除盘亏数量如何处理？
                            List<StockDetail> stockDetailList = stockDetailRepository.findByGoodsAndAmountGreaterThanOrderByCreateTimeAsc(detail.getGoods(), BigDecimal.ZERO);
                            BigDecimal before;
                            BigDecimal end = BigDecimal.ZERO;
                            int seq = 0;
                            for (StockDetail stockDetail : stockDetailList) {
                                before = stockDetail.getAmount();
                                if (stockDetail.getAmount().compareTo(changeAmount) >= 0) {
                                    end = before.subtract(changeAmount);
                                    stockDetail.setAmount(end);
                                    break;
                                } else {
                                    changeAmount = changeAmount.subtract(stockDetail.getAmount());
                                    stockDetail.setAmount(BigDecimal.ZERO);
                                }

                                this.stockDetailChangeRecordRepository.save(this.buildChangeRecordSub(changeAmount, before, end, seq, stockDetail, detail));
                                seq++;
                            }
                            this.stockDetailRepository.saveAll(stockDetailList);
                        }

                        //更新库存
                        Stock stock = detail.getGoods().getStock();
                        stock.setAmount(detail.getAmount());
                        stockRepository.save(stock);
                    }
                }

                //生成当前操作的操作日志
                StockBillOperateLog optLog = new StockBillOperateLog()
                        .setBill(bill)
                        .setOptType(Constants.OPT_TYPE.AUDIT)
                        .setOptUserId(SecurityUtils.getUserId())
                        .setOptUserName(SecurityUtils.getUserName())
                        .setTask(bill.getTask());
                optLog.setRemark(req.getRemark());
                optLog.setAuditResult(req.getOptType());
                this.stockBillOperateLogRepository.save(optLog);

                //更新单据状态
                bill.setStatus(Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH);
                bill.setTask(stockFlwConfigService.getEndTask(Constants.BILL_TYPE.STOCK_INVENTORY));
                bill.setAuditUserId(StringUtils.isNotBlank(bill.getAuditUserId()) ? "," + SecurityUtils.getUserId() : SecurityUtils.getUserId() + "");
                bill.setAuditUserName(StringUtils.isNotBlank(bill.getAuditUserName()) ? "," + SecurityUtils.getUserName() : SecurityUtils.getUserName());
                this.stockBillRepository.save(bill);

                //结束日志
                optLog = new StockBillOperateLog()
                        .setBill(bill)
                        .setOptType(Constants.OPT_TYPE.AUDIT)
                        .setOptUserId(SecurityUtils.getUserId())
                        .setOptUserName(SecurityUtils.getUserName())
                        .setTask(bill.getTask());
                optLog.setRemark(req.getRemark());
                optLog.setAuditResult(req.getOptType());
                this.stockBillOperateLogRepository.save(optLog);
            } else {
                //存在下一个审批任务
                Set<StockBillAuthority> authorities = buildBillAuthority(bill.getId(), null, nextTask.getHandlerId());
                if (!CollectionUtils.isEmpty(authorities)) {
                    bill.getAuthorities().addAll(authorities);
                }
                stockBillAuthorityRepository.saveAll(authorities);

                //操作日志
                StockBillOperateLog optLog = new StockBillOperateLog()
                        .setBill(bill)
                        .setOptType(Constants.OPT_TYPE.AUDIT)
                        .setOptUserId(SecurityUtils.getUserId())
                        .setOptUserName(SecurityUtils.getUserName())
                        .setTask(bill.getTask());
                optLog.setRemark(req.getRemark());
                optLog.setAuditResult(req.getOptType());
                this.stockBillOperateLogRepository.save(optLog);

                bill.setStatus(Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1);
                bill.setUpdateTime(optTime);
                bill.setTask(nextTask);
                bill.setAuditUserId(StringUtils.isNotBlank(bill.getAuditUserId()) ? "," + SecurityUtils.getUserId() : SecurityUtils.getUserId() + "");
                bill.setAuditUserName(StringUtils.isNotBlank(bill.getAuditUserName()) ? "," + SecurityUtils.getUserName() : SecurityUtils.getUserName());
                bill = this.stockBillRepository.save(bill);

                //生成下一个人待办
                StockTodo nextTodo = this.buildTodoList(bill, nextTask);
                this.stockTodoListRepository.save(nextTodo);

                //待办权限
                authorities = buildBillAuthority(bill.getId(), nextTodo.getId(), nextTask.getHandlerId());
                stockBillAuthorityRepository.saveAll(authorities);
            }
        }

        //更新待办为已办
        List<StockTodo> todoList = stockTodoListRepository.findByStockBillAndTaskAndStatus(bill, currentTask, Constants.TODO_STATUS_INIT);
        if (!CollectionUtils.isEmpty(todoList)) {
            for (StockTodo todo : todoList) {
                todo.setStatus(Constants.TODO_STATUS_DONE);
                todo.setUpdateTime(optTime);
            }
            this.stockTodoListRepository.saveAll(todoList);
        }
    }
}
