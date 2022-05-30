package com.iotinall.canteen.service;

import com.iotinall.canteen.annotations.FreshenMess;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.constants.FreshenMessEnum;
import com.iotinall.canteen.dto.inbill.StockInAcceptanceReq;
import com.iotinall.canteen.dto.inbill.StockInApplyReq;
import com.iotinall.canteen.dto.inbill.StockInDetailReq;
import com.iotinall.canteen.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 库存逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockBillInService extends StockBillCommonService {
    /**
     * 新建
     *
     * @author loki
     * @date 2020/08/26 10:48
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void create(StockInApplyReq req) {
        //当前任务
        StockFlwTask firstTask = getFirstTask(Constants.BILL_TYPE.STOCK_IN);

        //下一任务
        StockFlwTask nextTask = getNextTask(firstTask);

        StockBill bill;
        try {
            bill = (StockBill) initBill(nextTask)
                    .setBillType(Constants.BILL_TYPE.STOCK_IN)
                    .setBillDate(req.getBillDate())
                    .setBillNo(req.getBillNo())
                    .setSupplier(this.stockSupplierService.getSupplierById(req.getSupplierId()))
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

        //保存采购入库详情
        StockBillDetail detail;
        StockGoods goods;
        int index = 0;
        for (StockInDetailReq d : req.getDetails()) {
            //商品
            goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                    () -> new BizException("商品不存在")
            );

            detail = new StockBillDetail();
            detail.setSeq(++index);
            detail.setBill(bill);
            detail.setPrice(d.getPrice());
            detail.setGoods(goods);
            detail.setAmount(d.getAmount());
            detail.setRealAmount(d.getAmount());
            detail.setStockAmount(d.getStockAmount());
            this.stockBillDetailRepository.save(detail);

            //更新商品单价
            goods.setPrice(d.getPrice());
            this.stockGoodsRepository.save(goods);
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
     * @date 2021/6/4 15:56
     **/
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void update(StockInApplyReq req) {
        //单据
        StockBill bill = this.getBillByNo(req.getBillNo());

        canEdit(bill);

        bill.setSupplier(this.stockSupplierService.getSupplierById(req.getSupplierId()));
        bill.setBillDate(req.getBillDate());
        bill.setRemark(req.getRemark());
        bill.setVersion(System.currentTimeMillis());
        bill = this.stockBillRepository.save(bill);

        Set<StockBillDetail> detailListOld = bill.getDetails();

        //保存采购入库详情
        StockBillDetail detail;
        int index = 0;
        StockGoods goods;
        for (StockInDetailReq d : req.getDetails()) {
            //商品
            if (d.getGoodsId() == null) {
                throw new BizException("请选择商品");
            }

            goods = this.stockGoodsRepository.findById(d.getGoodsId()).orElseThrow(
                    () -> new BizException("商品不存在")
            );

            detail = new StockBillDetail();
            detail.setBill(bill);
            detail.setAmount(d.getAmount());
            detail.setRealAmount(d.getAmount());
            detail.setStockAmount(d.getStockAmount());
            detail.setPrice(d.getPrice());
            detail.setGoods(goods);
            detail.setSeq(++index);

            //已经验收的商品
            detail.setAcceptance(this.getBillDetailAcceptanceStatus(detailListOld, goods.getId()));
            this.stockBillDetailRepository.save(detail);

            goods.setPrice(d.getPrice());
            this.stockGoodsRepository.save(goods);
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
    public void acceptance(StockInAcceptanceReq req) {
        StockBill bill = this.getBillByNo(req.getBillNo());
        checkBillStatus(Constants.BILL_TYPE.STOCK_IN, bill, req.getVersion());

        StockGoods goods = this.stockGoodsRepository.findById(req.getGoodsId()).orElseThrow(
                () -> new BizException("商品不存在")
        );

        StockBillDetail billDetail = this.getStockBillDetail(bill, goods);

        billDetail.setAcceptance(true);
        billDetail.setShelfLife(req.getShelfLife());
        billDetail.setShelfLifeUnit(req.getShelfLifeUnit());
        billDetail.setProductionDate(req.getProductionDate());
        billDetail.setShelfLifeDate(this.calculateShelfLife(billDetail));
        billDetail.setInspectionReport(req.getInspectionReport());
        billDetail.setRealAmount(null == req.getRealAmount() ? BigDecimal.ZERO : req.getRealAmount());

        if (null != req.getFile()) {
            billDetail.setImgUrl(this.saveAcceptanceImage(req.getFile(), bill, billDetail));
        }
        this.stockBillDetailRepository.save(billDetail);

        //库存明细
        StockDetail stockDetail = new StockDetail();
        stockDetail.setAmount(billDetail.getRealAmount());
        stockDetail.setBillDetail(billDetail);
        stockDetail.setGoods(goods);
        this.stockDetailRepository.save(stockDetail);

        //商品库存
        Stock stock = goods.getStock();
        if (null == stock) {
            stock = stockService.initStock(billDetail.getRealAmount());
            goods.setStock(stock);
            this.stockGoodsRepository.save(goods);
        } else {
            stock.setAmount(stock.getAmount().add(billDetail.getRealAmount()));
            stockRepository.save(stock);
        }

        //更新库存记录
        this.stockDetailChangeRecordRepository.save(this.buildChangeRecordAdd(
                stockDetail.getAmount(),
                BigDecimal.ZERO,
                stockDetail.getAmount(),
                0,
                stockDetail,
                billDetail
        ));
    }

    /**
     * 电子秤验收
     *
     * @author loki
     * @date 2021/05/27 20:15
     */
    @FreshenMess({FreshenMessEnum.TODAY_STOCK_STAT, FreshenMessEnum.STOCK_WARN, FreshenMessEnum.STOCK_ACCEPT_CERT, FreshenMessEnum.STOCK_IN_COST_30})
    @Transactional(rollbackFor = Exception.class)
    public void acceptance(StockBillDetail billDetail, StockGoods goods) {
        /**
         * 更新库存
         */
        Stock stock = goods.getStock();
        if (null != stock) {
            stock.setAmount(stock.getAmount().add(billDetail.getRealAmount()));
        } else {
            stock = new Stock();
            stock.setCreateTime(LocalDateTime.now());
            stock.setAmount(billDetail.getRealAmount());
            goods.setStock(stock);
            this.stockGoodsRepository.save(goods);
        }
        stockRepository.save(stock);

        /**
         * 库存明细
         */
        StockDetail stockDetail = new StockDetail();
        stockDetail.setGoods(goods);
        stockDetail.setAmount(billDetail.getRealAmount());
        stockDetail.setBillDetail(billDetail);
        this.stockDetailRepository.save(stockDetail);

        //更新库存记录
        this.stockDetailChangeRecordRepository.save(this.buildChangeRecordAdd(
                stockDetail.getAmount(),
                BigDecimal.ZERO,
                stockDetail.getAmount(),
                0,
                stockDetail,
                billDetail
        ));
    }
}
