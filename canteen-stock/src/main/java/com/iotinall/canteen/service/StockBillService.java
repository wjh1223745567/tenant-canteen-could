package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.bill.*;
import com.iotinall.canteen.dto.goods.StockGoodsDTO;
import com.iotinall.canteen.dto.inbill.*;
import com.iotinall.canteen.dto.inventory.StockInventoryBillDTO;
import com.iotinall.canteen.dto.inventory.StockInventoryBillDetailDTO;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.outbill.*;
import com.iotinall.canteen.dto.stock.MasterMaterialDTO;
import com.iotinall.canteen.dto.supplier.StockSupplierDTO;
import com.iotinall.canteen.dto.warehouse.StockWarehouseDTO;
import com.iotinall.canteen.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 单据逻辑处理类
 * 这里只处理所有单据公共的接口，与业务相关的接口在各自的service中
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockBillService extends StockBillCommonService {
    @Resource
    private StockBillInService stockBillInService;
    @Resource
    private StockBillOutService stockBillOutService;
    @Resource
    private StockBillInBackService stockBillInBackService;
    @Resource
    private StockBillOutBackService stockBillOutBackService;

    /**
     * 采购入库，领用出库，采购退货，领用退库 申请单据 分页列表
     * 1、申请人可查看自己申请的单据
     * 2、审核人可查看自己待处理或者已处理的单据
     * 3、经办人可查看自己待处理或者已处理的单据
     *
     * @author loki
     * @date 2020/08/27 14:26
     */
    public Object page(StockBillQueryReq req, Pageable page) {
        List<Integer> statusList = new ArrayList<>();
        if (null != req.getStatus()) {
            //存在2个审核状态，前端默认传STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1
            if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1 == req.getStatus()) {
                statusList = Arrays.asList(
                        Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_0,
                        Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_EXAMINE_1
                );
            } else {
                statusList = Collections.singletonList(req.getStatus());
            }
        }

        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.gte("billDate", req.getBeginDate() == null ? null : req.getBeginDate()))
                .where(Criterion.lt("billDate", req.getEndDate() == null ? null : req.getEndDate().plusDays(1)))
                .where(Criterion.in("status", statusList))
                .where(Criterion.eq("billType", req.getBillType()))
                .where(Criterion.eq("supplier.id", req.getSupplierId()))
                .whereByOr(
                        Criterion.like("billNo", req.getKeyword()),
                        Criterion.like("details.goods.name", req.getKeyword()),
                        Criterion.like("applyUserName", req.getKeyword())
                );

        List<Criterion> authList = new ArrayList<>();
        Criterion criterion;

        //获取当前用户权限
        FeignEmployeeDTO employee = feignEmployeeService.findById(SecurityUtils.getUserId());
        for (Long roleId : employee.getRoleIds()) {
            criterion = Criterion.eq("authorities.roleId", roleId);
            authList.add(criterion);
        }
        authList.add(Criterion.eq("applyUserId", SecurityUtils.getUserId()));
        builder.whereByOr(authList);

        Page<StockBill> result = this.stockBillRepository.findAll(builder.build(true), page);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<StockBill> stockBillList = result.getContent();
        List<StockBillDTO> stockBillDTOList = new ArrayList<>();
        StockBillDTO stockBillDTO;
        StockDetail stockDetail;
        StockSupplierDTO supplier;
        StockWarehouseDTO warehouseDTO;
        for (StockBill bill : stockBillList) {
            stockBillDTO = new StockBillDTO();
            stockBillDTOList.add(stockBillDTO);

            BeanUtils.copyProperties(bill, stockBillDTO);
            stockBillDTO.setOrgName(bill.getApplyUserOrgName());
            if (null != bill.getSupplier()) {
                supplier = new StockSupplierDTO();
                BeanUtils.copyProperties(bill.getSupplier(), supplier);
                stockBillDTO.setSupplier(supplier);
            }

            if (null != bill.getWarehouse()) {
                warehouseDTO = new StockWarehouseDTO();
                BeanUtils.copyProperties(bill.getWarehouse(), warehouseDTO);
                stockBillDTO.setWarehouse(warehouseDTO);
            }

            //单据明细
            if (!CollectionUtils.isEmpty(bill.getDetails())) {
                Set<StockBillDetail> billDetails = bill.getDetails();
                StockBillDetailDTO detailDTO;
                List<StockBillDetailDTO> billDTOList = new ArrayList<>();
                for (StockBillDetail billDetail : billDetails) {
                    detailDTO = new StockBillDetailDTO();
                    billDTOList.add(detailDTO);
                    BeanUtils.copyProperties(billDetail, detailDTO);

                    //商品信息
                    if (null != billDetail.getGoods()) {
                        StockGoods stockGoods = billDetail.getGoods();
                        StockGoodsDTO stockGoodsDTO = new StockGoodsDTO();
                        detailDTO.setStockGoods(stockGoodsDTO);
                        BeanUtils.copyProperties(stockGoods, stockGoodsDTO);
                        stockGoodsDTO.setStockAmount(
                                stockGoods.getStock() == null ?
                                        BigDecimal.ZERO :
                                        stockGoods.getStock().getAmount());
                    }

                    //获取商品在该批次入库单中剩余未领用的数量
                    stockDetail = this.stockDetailRepository.findByBillDetail(billDetail);
                    detailDTO.setLeftAmount(null != stockDetail ? stockDetail.getAmount() : BigDecimal.ZERO);
                }

                stockBillDTO.setDetails(billDTOList);
            }
        }
        return PageUtil.toPageDTO(stockBillDTOList, result);
    }

    /**
     * 单据详情,根据不同的单据类型返回不同的内容
     *
     * @author loki
     * @date 2020/08/27 19:59
     */
    public Object log(String billNo) {
        StockBill bill = this.stockBillRepository.findByBillNo(billNo);
        if (bill == null) {
            throw new BizException("单据不存在");
        }

        //流程
        StockFlwConfig flwConfig = stockFlwConfigRepository.findByType(bill.getBillType());
        if (null == flwConfig) {
            throw new BizException("流程配置不存在");
        }

        List<StockFlwTask> taskList = stockFlwTaskRepository.findByFlwConfigIdAndVersionOrderByTaskIdAsc(flwConfig.getId(), bill.getTask().getVersion());

        StockBillLogDTO billLogDTO = new StockBillLogDTO();
        billLogDTO.setCurrentTaskId(bill.getTask().getTaskId());

        StockBillOperateLogDTO logDTO;
        List<StockBillLogDetailDTO> logDetailLogList = new ArrayList<>();
        StockBillLogDetailDTO logDetailDTO;
        for (StockFlwTask task : taskList) {
            logDetailDTO = new StockBillLogDetailDTO();
            logDetailLogList.add(logDetailDTO);
            logDetailDTO.setTaskId(task.getTaskId());
            logDetailDTO.setTaskDefine(task.getTaskDefine());

            //操作日志
            StockBillOperateLog operateLog = this.stockBillOperateLogRepository.findByBillAndTask(bill, task);
            if (null != operateLog) {
                logDTO = new StockBillOperateLogDTO();
                BeanUtils.copyProperties(operateLog, logDTO);
                logDetailDTO.setLog(logDTO);
            }
        }
        billLogDTO.setTask(logDetailLogList);

        return billLogDTO;
    }

    /**
     * 单据详情,根据不同的单据类型返回不同的内容
     * goodsId!=null 表示查询该商品的明细
     *
     * @author loki
     * @date 2020/08/27 19:59
     */
    public Object detail(String billNo, Long goodsId) {
        StockBill bill = this.stockBillRepository.findByBillNo(billNo);
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        switch (bill.getBillType()) {
            case Constants.BILL_TYPE.STOCK_IN: {
                return this.getStockInBillDetail(bill, goodsId);
            }
            case Constants.BILL_TYPE.STOCK_IN_BACK: {
                return this.getStockInBackBillDetail(bill, goodsId);
            }
            case Constants.BILL_TYPE.STOCK_OUT: {
                return this.getStockOutBillDetail(bill, goodsId);
            }
            case Constants.BILL_TYPE.STOCK_OUT_BACK: {
                return this.getStockOutBackBillDetail(bill, goodsId);
            }
            case Constants.BILL_TYPE.STOCK_INVENTORY: {
                return this.getStockInventoryBillDetail(bill);
            }
            default: {
                throw new BizException("未知单据类型");
            }
        }
    }

    /**
     * 领用退库
     *
     * @author loki
     * @date 2020/08/28 13:59
     */
    private BaseStockBillDTO getStockOutBackBillDetail(StockBill bill, Long goodsId) {
        StockOutBackBillDTO stockBillDTO = new StockOutBackBillDTO();
        BeanUtils.copyProperties(bill, stockBillDTO);
        stockBillDTO.setOutBillNo(bill.getOutBill().getBillNo());

        //退库单据明细
        if (!CollectionUtils.isEmpty(bill.getDetails())) {
            Set<StockBillDetail> details = bill.getDetails();
            StockOutBackBillDetailDTO detailDTO;
            List<StockOutBackBillDetailDTO> billDTOList = new ArrayList<>();

            if (null != goodsId) {
                StockBillDetail detail = details.stream().filter(item -> item.getGoods().getId().equals(goodsId)).findAny().get();
                detailDTO = genStockOutBackDetail(detail);
                billDTOList.add(detailDTO);
            } else {
                for (StockBillDetail detail : details) {
                    detailDTO = genStockOutBackDetail(detail);
                    billDTOList.add(detailDTO);
                }

                //用户操作权限
                List<String> options = this.genUserOptions(bill);
                stockBillDTO.setOptions(options);
            }
            stockBillDTO.setDetails(billDTOList);
        }

        StockBillApplyUserInfoDTO applyUserInfoDTO = new StockBillApplyUserInfoDTO();
        applyUserInfoDTO.setId(bill.getApplyUserId());
        applyUserInfoDTO.setName(bill.getApplyUserName());
        stockBillDTO.setApplyUserInfo(applyUserInfoDTO);

        return stockBillDTO;
    }

    /**
     * 组装退库详情
     *
     * @author loki
     * @date 2020/09/09 14:12
     */
    private StockOutBackBillDetailDTO genStockOutBackDetail(StockBillDetail detail) {
        StockOutBackBillDetailDTO detailDTO = new StockOutBackBillDetailDTO();
        BeanUtils.copyProperties(detail, detailDTO);

        //商品信息
        if (null != detail.getGoods()) {
            StockGoods stockGoods = detail.getGoods();
            StockGoodsDTO stockGoodsDTO = new StockGoodsDTO();
            detailDTO.setStockGoods(stockGoodsDTO);
            BeanUtils.copyProperties(stockGoods, stockGoodsDTO);

            if (null != stockGoods.getType()) {
                StockGoodsType goodsType = stockGoods.getType();
                stockGoodsDTO.setGoodsTypeId(goodsType.getId());
                stockGoodsDTO.setGoodsTypeName(goodsType.getName());
            }

            //获取该商品对应的库存信息
            stockGoodsDTO.setStockAmount(stockGoods.getStock() == null ? BigDecimal.ZERO : stockGoods.getStock().getAmount());

            //仓库信息
            if (null != stockGoods.getWarehouse()) {
                StockWarehouseDTO stockWarehouseDTO = new StockWarehouseDTO();
                BeanUtils.copyProperties(stockGoods.getWarehouse(), stockWarehouseDTO);
                stockGoodsDTO.setWarehouse(stockWarehouseDTO);
            }

            //退库对应出库单据
            StockBill outBill = detail.getBillDetail().getBill();
            StockBillDetail billDetail = detail.getBillDetail();
            if (null != billDetail) {
                StockBillDetailDTO outBillDetailDTO = new StockBillDetailDTO();
                BeanUtils.copyProperties(billDetail, outBillDetailDTO);
                outBillDetailDTO.setBillNo(outBill.getBillNo());
                outBillDetailDTO.setBillDate(outBill.getBillDate());
                detailDTO.setOutBill(outBillDetailDTO);
            }
        }

        detailDTO.setImgUrl(detail.getImgUrl());
        return detailDTO;
    }

    /**
     * 仓库盘点详情
     *
     * @author loki
     * @date 2021/8/4 16:41
     **/
    private StockInventoryBillDTO getStockInventoryBillDetail(StockBill bill) {
        StockInventoryBillDTO stockBillDTO = new StockInventoryBillDTO();
        BeanUtils.copyProperties(bill, stockBillDTO);

        if (null != bill.getWarehouse()) {
            StockWarehouseDTO warehouseDTO = new StockWarehouseDTO();
            BeanUtils.copyProperties(bill.getWarehouse(), warehouseDTO);
            stockBillDTO.setWarehouse(warehouseDTO);
        }

        if (!CollectionUtils.isEmpty(bill.getDetails())) {
            Set<StockBillDetail> detailList = bill.getDetails();
            List<StockInventoryBillDetailDTO> detailDTOList = new ArrayList<>();
            StockInventoryBillDetailDTO detailDTO;
            for (StockBillDetail detail : detailList) {
                detailDTO = new StockInventoryBillDetailDTO();
                detailDTOList.add(detailDTO);
                BeanUtils.copyProperties(detail, detailDTO);

                //商品信息
                if (null != detail.getGoods()) {
                    StockGoods stockGoods = detail.getGoods();
                    BeanUtils.copyProperties(stockGoods, detailDTO);
                    detailDTO.setGoodsId(stockGoods.getId());
                    detailDTO.setCode(stockGoods.getCode());
                    detailDTO.setLowerLimit(stockGoods.getLowerLimit());

                    if (null != stockGoods.getType()) {
                        detailDTO.setGoodsTypeId(stockGoods.getType().getId());
                        detailDTO.setGoodsTypeName(stockGoods.getType().getName());
                    }
                }
            }
            stockBillDTO.setBillDetails(detailDTOList);
        }

        //用户操作权限
        List<String> options = this.genUserOptions(bill);
        stockBillDTO.setOptions(options);

        StockBillApplyUserInfoDTO applyUserInfoDTO = new StockBillApplyUserInfoDTO();
        applyUserInfoDTO.setId(bill.getApplyUserId());
        applyUserInfoDTO.setName(bill.getApplyUserName());
        stockBillDTO.setApplyUserInfo(applyUserInfoDTO);
        return stockBillDTO;
    }

    /**
     * 领用出库详情
     *
     * @author loki
     * @date 2020/08/28 13:58
     */
    private StockOutBillDTO getStockOutBillDetail(StockBill bill, Long goodsId) {
        StockOutBillDTO stockBillDTO = new StockOutBillDTO();
        BeanUtils.copyProperties(bill, stockBillDTO);

        //单据明细
        if (!CollectionUtils.isEmpty(bill.getDetails())) {
            Set<StockBillDetail> details = bill.getDetails();
            StockOutBillDetailDTO detailDTO;
            List<StockOutBillDetailDTO> billDTOList = new ArrayList<>();
            if (null != goodsId) {
                StockBillDetail detail = details.stream().filter(item -> item.getGoods().getId().equals(goodsId)).findAny().get();
                detailDTO = this.genStockOutBillDetail(detail);
                billDTOList.add(detailDTO);
            } else {

                List<StockDetailChangeRecord> stockDetailChangeRecordList;
                for (StockBillDetail detail : details) {
                    detailDTO = this.genStockOutBillDetail(detail);
                    billDTOList.add(detailDTO);

                    /**
                     * 2021-04-25 已验收完成的出库单，可能对应多个单价，这里返回多个单价
                     */
                    List<StockDetailChangeRecordDTO> changeRecordList = new ArrayList<>();
                    if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH == bill.getStatus()) {
                        stockDetailChangeRecordList = stockDetailChangeRecordRepository.findByBillDetailOrderBySeqDesc(detail);
                        StockDetailChangeRecordDTO changeRecordDTO;
                        if (!CollectionUtils.isEmpty(stockDetailChangeRecordList)) {
                            for (StockDetailChangeRecord detailChangeRecord : stockDetailChangeRecordList) {
                                changeRecordDTO = new StockDetailChangeRecordDTO();
                                changeRecordList.add(changeRecordDTO);
                                changeRecordDTO.setAmount(detailChangeRecord.getAmount());
                                changeRecordDTO.setPrice(detailChangeRecord.getStockDetail().getBillDetail().getPrice());
                            }
                        }
                    } else {
                        StockDetailChangeRecordDTO changeRecordDTO = new StockDetailChangeRecordDTO();
                        changeRecordDTO.setAmount(detail.getAmount());
                        changeRecordDTO.setPrice(detail.getPrice());
                        changeRecordList.add(changeRecordDTO);
                    }

                    detailDTO.setImgUrl(detail.getImgUrl());
                    detailDTO.setOutDetails(changeRecordList);
                }

                stockBillDTO.setDetails(billDTOList);

                //用户操作权限
                List<String> options = this.genUserOptions(bill);
                stockBillDTO.setOptions(options);
            }
            stockBillDTO.setDetails(billDTOList);
        }

        //申请人信息
        StockBillApplyUserInfoDTO applyUserInfoDTO = new StockBillApplyUserInfoDTO();
        applyUserInfoDTO.setId(bill.getApplyUserId());
        applyUserInfoDTO.setName(bill.getApplyUserName());
        applyUserInfoDTO.setOrgName(bill.getApplyUserOrgName());
        stockBillDTO.setApplyUserInfo(applyUserInfoDTO);

        return stockBillDTO;
    }

    /**
     * 组装出库明细
     *
     * @author loki
     * @date 2020/09/09 14:05
     */
    private StockOutBillDetailDTO genStockOutBillDetail(StockBillDetail detail) {
        StockOutBillDetailDTO detailDTO = new StockOutBillDetailDTO();
        BeanUtils.copyProperties(detail, detailDTO);

        //商品信息
        if (null != detail.getGoods()) {
            StockGoods stockGoods = detail.getGoods();
            StockGoodsDTO stockGoodsDTO = new StockGoodsDTO();
            detailDTO.setStockGoods(stockGoodsDTO);
            BeanUtils.copyProperties(stockGoods, stockGoodsDTO);

            if (null != stockGoods.getType()) {
                StockGoodsType goodsType = stockGoods.getType();
                stockGoodsDTO.setGoodsTypeId(goodsType.getId());
                stockGoodsDTO.setGoodsTypeName(goodsType.getName());
            }

            //获取该商品对应的库存信息
            stockGoodsDTO.setStockAmount(stockGoods.getStock() == null ? BigDecimal.ZERO : stockGoods.getStock().getAmount());

            //仓库信息
            if (null != stockGoods.getWarehouse()) {
                StockWarehouseDTO stockWarehouseDTO = new StockWarehouseDTO();
                BeanUtils.copyProperties(stockGoods.getWarehouse(), stockWarehouseDTO);
                stockGoodsDTO.setWarehouse(stockWarehouseDTO);
            }
        }

        return detailDTO;
    }

    /**
     * 采购退货
     *
     * @author loki
     * @date 2020/08/28 13:59
     */
    private StockInBackBillDTO getStockInBackBillDetail(StockBill bill, Long goodsId) {
        StockInBackBillDTO stockBillDTO = new StockInBackBillDTO();
        BeanUtils.copyProperties(bill, stockBillDTO);

        //单据明细
        if (!CollectionUtils.isEmpty(bill.getDetails())) {
            Set<StockBillDetail> details = bill.getDetails();
            StockInBackBillDetailDTO detailDTO;
            List<StockInBackBillDetailDTO> billDTOList = new ArrayList<>();
            StockBillDetail billDetail;
            if (null != goodsId) {
                billDetail = details.stream().filter(item -> item.getGoods().getId().equals(goodsId)).findAny().get();
                detailDTO = this.genStockInBackBillDetail(billDetail);
                billDTOList.add(detailDTO);
            } else {

                for (StockBillDetail detail : details) {
                    detailDTO = this.genStockInBackBillDetail(detail);
                    detailDTO.setImgUrl(detail.getImgUrl());
                    billDTOList.add(detailDTO);
                }

                //用户操作权限
                List<String> options = this.genUserOptions(bill);
                stockBillDTO.setOptions(options);
            }
            stockBillDTO.setDetails(billDTOList);
        }

        //供应商
        if (null != bill.getSupplier()) {
            StockSupplierDTO supplierV2DTO = new StockSupplierDTO();
            BeanUtils.copyProperties(bill.getSupplier(), supplierV2DTO);
            supplierV2DTO.setSupplierTypeId(bill.getSupplier().getId());
            supplierV2DTO.setSupplierTypeName(bill.getSupplier().getName());
            stockBillDTO.setSupplier(supplierV2DTO);
        }

        //申请人信息
        StockBillApplyUserInfoDTO applyUserInfoDTO = new StockBillApplyUserInfoDTO();
        applyUserInfoDTO.setId(bill.getApplyUserId());
        applyUserInfoDTO.setName(bill.getApplyUserName());
        stockBillDTO.setApplyUserInfo(applyUserInfoDTO);

        return stockBillDTO;
    }

    /**
     * 组装商品退库明细
     *
     * @author loki
     * @date 2020/09/09 13:56
     */
    private StockInBackBillDetailDTO genStockInBackBillDetail(StockBillDetail detail) {
        StockInBackBillDetailDTO detailDTO = new StockInBackBillDetailDTO();
        BeanUtils.copyProperties(detail, detailDTO);

        //商品信息
        if (null != detail.getGoods()) {
            StockGoods goods = detail.getGoods();
            StockGoodsDTO stockGoodsDTO = new StockGoodsDTO();
            detailDTO.setStockGoods(stockGoodsDTO);
            BeanUtils.copyProperties(goods, stockGoodsDTO);

            if (null != goods.getType()) {
                StockGoodsType goodsType = goods.getType();
                stockGoodsDTO.setGoodsTypeId(goodsType.getId());
                stockGoodsDTO.setGoodsTypeName(goodsType.getName());
            }

            //获取该商品对应的库存信息
            stockGoodsDTO.setStockAmount(null == goods.getStock() ? BigDecimal.ZERO : goods.getStock().getAmount());

            //仓库信息
            if (null != goods.getWarehouse()) {
                StockWarehouseDTO stockWarehouseDTO = new StockWarehouseDTO();
                BeanUtils.copyProperties(goods.getWarehouse(), stockWarehouseDTO);
                stockGoodsDTO.setWarehouse(stockWarehouseDTO);
            }

            //入库单据
            if (null != detail.getBillDetail()) {
                StockBill inBill = detail.getBillDetail().getBill();
                StockBillDetail billDetail = this.stockBillDetailRepository.findByBillAndGoods(inBill, goods);
                if (null != billDetail) {
                    StockBillDetailDTO inBillDetailDTO = new StockBillDetailDTO();
                    BeanUtils.copyProperties(billDetail, inBillDetailDTO);
                    inBillDetailDTO.setBillNo(inBill.getBillNo());
                    inBillDetailDTO.setBillDate(inBill.getBillDate());
                    detailDTO.setInBill(inBillDetailDTO);
                }
            }
        }

        return detailDTO;
    }

    /**
     * 组装入库明细
     */
    private StockInBillDetailDTO genStockInBillDetailDTO(StockBillDetail detail) {
        StockInBillDetailDTO detailDTO = new StockInBillDetailDTO();
        BeanUtils.copyProperties(detail, detailDTO);

        //商品信息
        if (null != detail.getGoods()) {
            StockGoods stockGoods = detail.getGoods();
            StockGoodsDTO stockGoodsDTO = new StockGoodsDTO();
            detailDTO.setStockGoods(stockGoodsDTO);
            BeanUtils.copyProperties(stockGoods, stockGoodsDTO);

            if (null != stockGoods.getType()) {
                StockGoodsType goodsType = stockGoods.getType();
                stockGoodsDTO.setGoodsTypeId(goodsType.getId());
                stockGoodsDTO.setGoodsTypeName(goodsType.getName());
            }

            //获取该商品对应的库存信息
            stockGoodsDTO.setStockAmount(null == stockGoods.getStock() ? BigDecimal.ZERO : stockGoods.getStock().getAmount());

            //仓库信息
            if (null != stockGoods.getWarehouse()) {
                StockWarehouseDTO stockWarehouseDTO = new StockWarehouseDTO();
                BeanUtils.copyProperties(stockGoods.getWarehouse(), stockWarehouseDTO);
                stockGoodsDTO.setWarehouse(stockWarehouseDTO);
            }

        }
        detailDTO.setImgUrl(detail.getImgUrl());

        /**
         * 小程序一键出库，剩余库存
         */
        StockDetail stockDetail = stockDetailRepository.findByBillDetail(detail);
        detailDTO.setLeftAmount(null != stockDetail ? stockDetail.getAmount() : BigDecimal.ZERO);

        return detailDTO;
    }

    /**
     * 装成DTO
     *
     * @author loki
     * @date 2020/08/27 16:37
     */
    private StockInBillDTO getStockInBillDetail(StockBill bill, Long goodsId) {
        StockInBillDTO stockBillDTO = new StockInBillDTO();
        BeanUtils.copyProperties(bill, stockBillDTO);

        //单据明细
        if (!CollectionUtils.isEmpty(bill.getDetails())) {
            Set<StockBillDetail> details = bill.getDetails();
            StockInBillDetailDTO detailDTO;
            List<StockInBillDetailDTO> billDTOList = new ArrayList<>();
            if (null == goodsId) {
                for (StockBillDetail detail : details) {
                    detailDTO = genStockInBillDetailDTO(detail);
                    billDTOList.add(detailDTO);
                }

                //用户操作权限
                List<String> options = this.genUserOptions(bill);
                stockBillDTO.setOptions(options);
            } else {
                StockBillDetail detail = details.stream().filter(item -> item.getGoods().getId().equals(goodsId)).findAny().get();
                detailDTO = genStockInBillDetailDTO(detail);
                billDTOList.add(detailDTO);
            }
            stockBillDTO.setDetails(billDTOList);
        }

        //供应商
        if (null != bill.getSupplier()) {
            StockSupplierDTO supplierV2DTO = new StockSupplierDTO();
            BeanUtils.copyProperties(bill.getSupplier(), supplierV2DTO);
            supplierV2DTO.setSupplierTypeId(bill.getSupplier().getId());
            supplierV2DTO.setSupplierTypeName(bill.getSupplier().getName());
            stockBillDTO.setSupplier(supplierV2DTO);
        }

        //申请人信息
        StockBillApplyUserInfoDTO applyUserInfoDTO = new StockBillApplyUserInfoDTO();
        applyUserInfoDTO.setId(bill.getApplyUserId());
        applyUserInfoDTO.setName(bill.getApplyUserName());
        stockBillDTO.setApplyUserInfo(applyUserInfoDTO);

        return stockBillDTO;
    }

    /**
     * 单据审批
     *
     * @author loki
     * @date 2020/08/26 20:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void audit(StockAuditReq req) {
        //获取单据内容
        StockBill bill = this.getBillByNo(req.getBillNo());
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

        //审批操作日志
        StockBillOperateLog optLog = (StockBillOperateLog) new StockBillOperateLog()
                .setTask(bill.getTask())
                .setAuditResult(req.getOptType())
                .setBill(bill)
                .setOptUserId(SecurityUtils.getUserId())
                .setOptUserName(SecurityUtils.getUserName())
                .setOptType(Constants.OPT_TYPE.AUDIT)
                .setRemark(req.getRemark());
        this.stockBillOperateLogRepository.save(optLog);

        //更新待办为已办
        List<StockTodo> todoList = stockTodoListRepository.findByStockBill(bill);
        if (!CollectionUtils.isEmpty(todoList)) {
            for (StockTodo todo : todoList) {
                todo.setStatus(Constants.TODO_STATUS_DONE);
            }
            this.stockTodoListRepository.saveAll(todoList);
        }

        //拒绝
        if (req.getOptType() == Constants.REFUSE) {
            //更新单据
            bill.setFinishDate(LocalDate.now());
            bill.setStatus(Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_REFUSE);
            bill.setAuditUserId(StringUtils.isNotBlank(bill.getAuditUserId()) ? "," + SecurityUtils.getUserId() : SecurityUtils.getUserId() + "");
            bill.setAuditUserName(StringUtils.isNotBlank(bill.getAuditUserName()) ? "," + SecurityUtils.getUserName() : SecurityUtils.getUserName());

            //结束节点任务
            bill.setTask(this.stockFlwConfigService.getTask(bill.getBillType(), Constants.END_TASK_ID, bill.getTask().getVersion()));
            bill = this.stockBillRepository.save(bill);

            //结束日志
            StockBillOperateLog endLog = new StockBillOperateLog();
            BeanUtils.copyProperties(optLog, endLog);
            endLog.setTask(bill.getTask());
            this.stockBillOperateLogRepository.save(endLog);
        } else {
            //同意
            StockFlwTask nextTask = this.getNextTask(bill.getTask());

            //单据权限
            Set<StockBillAuthority> authorities = buildBillAuthority(bill.getId(), null, nextTask.getHandlerId());
            if (!CollectionUtils.isEmpty(authorities)) {
                bill.getAuthorities().addAll(authorities);
            }
            stockBillAuthorityRepository.saveAll(authorities);

            //单据
            bill.setTask(nextTask);
            bill.setStatus(getAuditBillStatus(nextTask));
            bill.setAuditUserId(StringUtils.isNotBlank(bill.getAuditUserId()) ?
                    "," + SecurityUtils.getUserId() : SecurityUtils.getUserId() + "");
            bill.setAuditUserName(StringUtils.isNotBlank(bill.getAuditUserName()) ?
                    "," + SecurityUtils.getUserName() : SecurityUtils.getUserName());

            this.stockBillRepository.save(bill);

            //生成下一个人待办
            StockTodo nextTodo = this.buildTodoList(bill, nextTask);
            this.stockTodoListRepository.save(nextTodo);

            //待办权限
            this.stockBillAuthorityRepository.saveAll(this.buildBillAuthority(bill.getId(),
                    nextTodo.getId(),
                    nextTask.getHandlerId()));
        }
    }

    /**
     * 单据取消
     *
     * @author loki
     * @date 2020/08/26 20:28
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String billNo) {
        //获取单据内容
        StockBill bill = this.getBillByNo(billNo);

        //判断是否可以取消
        if (bill.getStatus() == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH
                || bill.getStatus() == Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_REFUSE) {
            throw new BizException("单据已完成，不能取消");
        }

        //操作日志
        StockBillOperateLog optLog = new StockBillOperateLog()
                .setTask(bill.getTask())
                .setBill(bill)
                .setOptUserId(SecurityUtils.getUserId())
                .setOptUserName(SecurityUtils.getUserName())
                .setOptType(Constants.OPT_TYPE.CANCEL);
        this.stockBillOperateLogRepository.save(optLog);

        //更新待办为已办
        List<StockTodo> todoList = stockTodoListRepository.findByStockBill(bill);
        if (!CollectionUtils.isEmpty(todoList)) {
            for (StockTodo todo : todoList) {
                todo.setStatus(Constants.TODO_STATUS_DONE);
            }
            this.stockTodoListRepository.saveAll(todoList);
        }

        bill.setStatus(Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_CANCEL);
        bill.setTask(this.stockFlwConfigService.getTask(
                bill.getBillType(),
                Constants.END_TASK_ID,
                bill.getTask().getVersion()
        ));
        bill = this.stockBillRepository.save(bill);

        //结束日志
        StockBillOperateLog endLog = new StockBillOperateLog()
                .setTask(bill.getTask())
                .setBill(bill)
                .setOptUserId(SecurityUtils.getUserId())
                .setOptUserName(SecurityUtils.getUserName())
                .setOptType(Constants.OPT_TYPE.CANCEL);
        this.stockBillOperateLogRepository.save(endLog);
    }

    /**
     * 验收
     *
     * @author loki
     * @date 2020/08/27 9:51
     */
    @Transactional(rollbackFor = Exception.class)
    public void acceptance(String billNo) {
        StockBill bill = this.getBillByNo(billNo);
        if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_CANCEL == bill.getStatus()) {
            throw new BizException("单据已被取消");
        }

        if (Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE != bill.getStatus()) {
            throw new BizException("单据已被处理");
        }

        //校验是否还存在未被验收的商品
        for (StockBillDetail detail : bill.getDetails()) {
            if (!detail.getAcceptance()) {
                throw new BizException("存在未验收的商品");
            }
        }

        //更新待办为已办
        List<StockTodo> todoList = stockTodoListRepository.findByStockBill(bill);
        if (!CollectionUtils.isEmpty(todoList)) {
            for (StockTodo todo : todoList) {
                todo.setStatus(Constants.TODO_STATUS_DONE);
            }
            this.stockTodoListRepository.saveAll(todoList);
        }

        //操作日志
        StockBillOperateLog optLog = new StockBillOperateLog()
                .setTask(bill.getTask())
                .setBill(bill)
                .setOptUserId(SecurityUtils.getUserId())
                .setOptUserName(SecurityUtils.getUserName())
                .setOptType(Constants.OPT_TYPE.ACCEPTANCE);
        this.stockBillOperateLogRepository.save(optLog);

        //单据
        bill.setStatus(Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH);
        bill.setFinishDate(LocalDate.now());
        bill.setTask(this.stockFlwConfigService.getTask(
                bill.getBillType(),
                Constants.END_TASK_ID,
                bill.getTask().getVersion()
        ));
        bill.setAcceptanceUserId(SecurityUtils.getUserId());
        bill.setAcceptanceUserName(SecurityUtils.getUserName());
        this.stockBillRepository.save(bill);

        //生成结束日志
        StockBillOperateLog endLog = new StockBillOperateLog()
                .setTask(bill.getTask())
                .setBill(bill)
                .setOptUserId(SecurityUtils.getUserId())
                .setOptUserName(SecurityUtils.getUserName())
                .setOptType(Constants.OPT_TYPE.ACCEPTANCE);
        this.stockBillOperateLogRepository.save(endLog);
    }

    /**
     * (入库，出库，退库，退货)明细列表
     *
     * @author loki
     * @date 2021/6/8 16:06
     **/
    public Object detailList(StockBillDetailQueryReq req, Pageable page) {
        Sort sort = Sort.by(Arrays.asList(Sort.Order.desc("bill.bill_date"), Sort.Order.asc("seq")));
        page = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);

        //获取类别所有的子类别
        Set<Long> goodsTypeIdList = this.stockGoodsTypeService.getGoodsTypeChildIds(req.getGoodsTypeId());

        //获取所有的子仓库/位置ID
        Set<Long> warehouseIdList = stockWarehouseService.getWarehouseAllChildIds(req.getWarehouseId());

        //获取所有的子部门,只有出库单有部门
        Set<Long> orgIdList = null;
        if (null != req.getOrgId()) {
            orgIdList = feignOrgService.getAllChildOrg(Collections.singletonList(req.getOrgId()));
        }

        Page<StockBillDetail> result = this.stockBillDetailRepository.search(req.getBeginDate() == null ? null : req.getBeginDate(),
                req.getEndDate() == null ? null : req.getEndDate(),
                goodsTypeIdList,
                req.getBillNo(),
                req.getSupplierId(),
                warehouseIdList,
                req.getBillType(),
                orgIdList,
                req.getGoodsId(),
                req.getKeyword(),
                page);

        List<StockBillDetail> recordList = result.getContent();
        List<StockBillDetailInfoDTO> detailList = new ArrayList<>();
        StockBillDetailInfoDTO detailInfoDTO;
        for (StockBillDetail record : recordList) {
            detailInfoDTO = new StockBillDetailInfoDTO();
            detailList.add(detailInfoDTO);
            detailInfoDTO.setBillDetailId(record.getId());
            detailInfoDTO.setBillDate(record.getBill().getBillDate());
            detailInfoDTO.setBillNo(record.getBill().getBillNo());
            detailInfoDTO.setBillType(record.getBill().getBillType());
            if (null != record.getBill().getSupplier()) {
                detailInfoDTO.setSupplierName(record.getBill().getSupplier().getName());
            }

            if (null != record.getGoods()) {
                detailInfoDTO.setGoodsId(record.getGoods().getId());
                detailInfoDTO.setGoodsCode(record.getGoods().getCode());
                detailInfoDTO.setGoodsName(record.getGoods().getName());
                detailInfoDTO.setUnit(record.getGoods().getUnit());
                detailInfoDTO.setPrice(record.getPrice());
                detailInfoDTO.setSpecs(record.getGoods().getSpecs());
                detailInfoDTO.setImgUrl(record.getGoods().getImgUrl());

                if (null != record.getGoods().getType()) {
                    detailInfoDTO.setGoodsTypeName(record.getGoods().getType().getName());
                }

                if (null != record.getGoods().getWarehouse()) {
                    detailInfoDTO.setWarehouseName(record.getGoods().getWarehouse().getFullName());
                }
            }

            detailInfoDTO.setAmount(record.getAmount());
            detailInfoDTO.setApplyUserName(record.getBill().getApplyUserName());
            detailInfoDTO.setOrgId(record.getBill().getApplyUserOrgId());
            detailInfoDTO.setOrgName(record.getBill().getApplyUserOrgName());
            detailInfoDTO.setRealAmount(record.getRealAmount());

            /**
             * 退库，商品剩余可退商品数
             */
            if (record.getBill().getBillType().equals(Constants.BILL_TYPE.STOCK_OUT)) {
                BigDecimal outBackTotalAmount = getOutBackTotal(record.getId());
                detailInfoDTO.setLeftAmount(record.getRealAmount().subtract(null == outBackTotalAmount ? BigDecimal.ZERO : outBackTotalAmount));
            }
        }
        return PageUtil.toPageDTO(detailList, result);
    }

    /**
     * 一键验收
     *
     * @author loki
     * @date 2021/6/10 11:03
     **/
    public void multiAcceptance(StockMultiAcceptanceReq req) {
        if (CollectionUtils.isEmpty(req.getDetailIds())) {
            throw new BizException("请选择需要验收的商品");
        }
        StockBill bill = this.stockBillRepository.findByBillNo(req.getBillNo());
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        if (bill.getStatus() != Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_WAIT_ACCEPTANCE) {
            throw new BizException("单据状态异常");
        }

        switch (bill.getBillType()) {
            case Constants.BILL_TYPE.STOCK_IN: {
                StockInAcceptanceReq r = new StockInAcceptanceReq();
                r.setBillNo(req.getBillNo());
                r.setVersion(req.getVersion());
                if (!CollectionUtils.isEmpty(bill.getDetails())) {
                    for (StockBillDetail detail : bill.getDetails()) {
                        if (detail.getAcceptance()) {
                            continue;
                        }
                        if (!req.getDetailIds().add(detail.getId())) {
                            r.setGoodsId(detail.getGoods().getId());
                            r.setRealAmount(detail.getAmount());
                            stockBillInService.acceptance(r);
                        }
                    }
                }
                break;
            }
            case Constants.BILL_TYPE.STOCK_IN_BACK: {
                StockInBackAcceptanceReq r = new StockInBackAcceptanceReq();
                r.setBillNo(req.getBillNo());
                r.setVersion(req.getVersion());
                if (!CollectionUtils.isEmpty(bill.getDetails())) {
                    for (StockBillDetail detail : bill.getDetails()) {
                        if (detail.getAcceptance()) {
                            continue;
                        }
                        if (!req.getDetailIds().add(detail.getId())) {
                            r.setGoodsId(detail.getGoods().getId());
                            r.setRealAmount(detail.getAmount());
                            stockBillInBackService.acceptance(r);
                        }
                    }
                }

                break;
            }
            case Constants.BILL_TYPE.STOCK_OUT: {
                StockOutAcceptanceReq r = new StockOutAcceptanceReq();
                r.setBillNo(req.getBillNo());
                r.setVersion(req.getVersion());
                if (!CollectionUtils.isEmpty(bill.getDetails())) {
                    for (StockBillDetail detail : bill.getDetails()) {
                        if (detail.getAcceptance()) {
                            continue;
                        }
                        if (!req.getDetailIds().add(detail.getId())) {
                            r.setGoodsId(detail.getGoods().getId());
                            r.setRealAmount(detail.getAmount());
                            stockBillOutService.acceptance(r);
                        }
                    }
                }
                break;
            }
            case Constants.BILL_TYPE.STOCK_OUT_BACK: {
                StockOutBackAcceptanceReq r = new StockOutBackAcceptanceReq();
                r.setBillNo(req.getBillNo());
                r.setVersion(req.getVersion());
                if (!CollectionUtils.isEmpty(bill.getDetails())) {
                    for (StockBillDetail detail : bill.getDetails()) {
                        if (detail.getAcceptance()) {
                            continue;
                        }
                        if (!req.getDetailIds().add(detail.getId())) {
                            r.setGoodsId(detail.getGoods().getId());
                            r.setRealAmount(detail.getAmount());
                            stockBillOutBackService.acceptance(r);
                        }
                    }
                }
                break;
            }
            default: {
                throw new BizException("未知单据类型");
            }
        }
    }

    /**
     * 上传图片
     *
     * @author loki
     * @date 2020/09/21 20:38
     */
    public void uploadImg(MultipartFile file, String billNo, Long goodId, Boolean rotate) {
        if (null == file || file.isEmpty()) {
            return;
        }

        StockBill bill = this.stockBillRepository.findByBillNo(billNo);
        if (null == bill) {
            throw new BizException("单据不存在");
        }

        //商品
        StockGoods goods = stockGoodsRepository.findById(goodId).orElse(null);
        if (null == goods) {
            throw new BizException("商品不存在");
        }

        //获取单据明细
        StockBillDetail billDetail = this.stockBillDetailRepository.findByBillAndGoods(bill, goods);
        if (null == billDetail) {
            throw new BizException("单据明细不存在");
        }

        billDetail.setImgUrl(saveAcceptanceImage(file, bill, billDetail));
        this.stockBillDetailRepository.save(billDetail);
    }

    /**
     * 获取溯源链
     *
     * @author loki
     * @date 2021/8/10 10:27
     **/
    public List<MasterMaterialDTO> getSourceChain(Set<Long> masterMaterialIds) {
        List<MasterMaterialDTO> materialList = new ArrayList<>();
        MasterMaterialDTO menuMaterial;
        StockGoods stockGoods;
        for (Long materialId : masterMaterialIds) {
            try {
                stockGoods = stockGoodsRepository.findById(materialId).orElse(null);
                if (null == stockGoods) {
                    continue;
                }
            } catch (Exception ex) {
                continue;
            }

            menuMaterial = new MasterMaterialDTO();
            materialList.add(menuMaterial);
            menuMaterial.setMaterialName(stockGoods.getName());
            menuMaterial.setMaterialId(stockGoods.getId());

            //当日出库单
            SpecificationBuilder sb = SpecificationBuilder.builder()
                    .where(Criterion.eq("bill.billType", Constants.BILL_TYPE.STOCK_OUT))
                    .where(Criterion.eq("acceptance", true))
                    .where(Criterion.gt("realAmount", 0))
                    .where(Criterion.eq("goods.id", menuMaterial.getMaterialId()))
                    .where(Criterion.eq("bill.billDate", LocalDate.now()));
            List<StockBillDetail> billList = stockBillDetailRepository.findAll(sb.build(), Sort.by(Sort.Direction.DESC, "createTime"));

            if (CollectionUtils.isEmpty(billList)) {
                continue;
            }

            StockBillDetail stockBillDetail = billList.get(0);
            if (null != stockBillDetail.getBill()) {
                menuMaterial.setOutAccepter(stockBillDetail.getBill().getAcceptanceUserName());
                menuMaterial.setOutApplyer(stockBillDetail.getBill().getApplyUserName());
            }

            List<StockDetailChangeRecord> changeRecordList = this.stockDetailChangeRecordRepository.findByBillDetailOrderBySeqDesc(stockBillDetail);
            if (!CollectionUtils.isEmpty(changeRecordList)) {
                StockDetailChangeRecord changeRecord = changeRecordList.get(0);

                //出库商品对应入库单
                StockDetail stockDetail = changeRecord.getStockDetail();
                if (null != stockDetail && null != stockDetail.getBillDetail().getBill()) {
                    StockBill inBill = stockDetail.getBillDetail().getBill();
                    menuMaterial.setInApplyer(inBill.getApplyUserName());
                    menuMaterial.setInAccepter(inBill.getAcceptanceUserName());
                    menuMaterial.setSupplierName(inBill.getSupplier().getName());
                }
            }
        }

        return materialList;
    }
}
