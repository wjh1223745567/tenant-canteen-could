package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.inbill.StockInApplyReq;
import com.iotinall.canteen.dto.inbill.StockInDetailReq;
import com.iotinall.canteen.dto.procurementplan.*;
import com.iotinall.canteen.entity.Stock;
import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockBillOperateLog;
import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.StockBillOperateLogRepository;
import com.iotinall.canteen.repository.StockBillRepository;
import com.iotinall.canteen.repository.StockGoodsRepository;
import com.iotinall.canteen.dto.stock.FeignProcurementDto;
import com.iotinall.canteen.utils.CodeGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智慧采购计划
 */

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ProcurementPlanService {
    @Resource
    private FeignProcurementPlanService feignProcurementPlanService;
    @Resource
    private StockGoodsRepository stockGoodsRepository;
    @Resource
    private StockBillRepository stockBillRepository;
    @Resource
    private StockBillOperateLogRepository operateLogRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private StockBillInService inService;

    /**
     * 创建入库订单
     */
    public void createInOrder(List<InOrderReq> inOrderReq) {
        for (InOrderReq orderReq : inOrderReq) {
            StockInApplyReq req = new StockInApplyReq();
            req.setBillDate(LocalDate.now());
            req.setSupplierId(orderReq.getSupplierId());
            req.setBillNo(CodeGeneratorUtil.buildCode(0, 0));

            List<StockInDetailReq> inDetailReqs = new ArrayList<>();
            for (InOrderProdReq inOrderProdReq : orderReq.getProd()) {
                Optional<StockGoods> optionalStockGoods = stockGoodsRepository.findById(inOrderProdReq.getId());
                Stock stockV2 = optionalStockGoods.get().getStock();
                StockInDetailReq detailReq = new StockInDetailReq();
                detailReq.setAmount(inOrderProdReq.getCount());
                detailReq.setGoodsId(inOrderProdReq.getId());
                detailReq.setPrice(inOrderProdReq.getPrice());
                detailReq.setStockAmount(stockV2 != null ? stockV2.getAmount() : BigDecimal.ZERO);
                detailReq.setInspectionReport(Boolean.FALSE);
                inDetailReqs.add(detailReq);
            }
            req.setDetails(inDetailReqs);
            inService.create(req);
        }
    }

    /**
     * 采购计划查询原料用料
     *
     * @return
     */
    public FeignProcurementDto findProductProcurement(LocalDate date) {
        return feignProcurementPlanService.findProductProcurement(date);
    }

    /**
     * 原料
     * 根据商品ID查询
     */
    public FeignProcurementDto.MenuProd traceabilityChain(Long id) {
        return feignProcurementPlanService.traceabilityChain(id);
    }

    /**
     * 查询缓存名称库存ID
     *
     * @return
     */
    public List<StockProdDto> findCacheName(List<String> prodIds) {
        List<StockProdDto> vectorProdDtos = new ArrayList<>();
        for (String id : prodIds) {
            Long longId = null;
            try {
                longId = Long.valueOf(id);
            } catch (NumberFormatException e) {
                continue;
            }
            Optional<StockGoods> optionalStockGoods = this.stockGoodsRepository.findById(longId);
            if (optionalStockGoods.isPresent()) {
                StockGoods stockGood = optionalStockGoods.get();
                StockProdDto vectorProdDto = new StockProdDto()
                        .setId(stockGood.getId())
                        .setPrice(stockGood.getPrice())
                        .setName(stockGood.getName())
                        .setSpecs(stockGood.getSpecs())
                        .setTrueName(stockGood.getName())
                        .setCode(stockGood.getCode())
                        .setTypeName(stockGood.getType() != null ? stockGood.getType().getName() : null);
                vectorProdDtos.add(vectorProdDto);
            }
        }
        return vectorProdDtos;
    }

    /**
     * 查询真实溯源链
     *
     * @param prodIds
     * @return
     */
    public List<List<ApplyChainDto>> link(List<String> prodIds, LocalDate date) {
        Specification<StockBill> stockBillSpecification = SpecificationBuilder.builder()
                //查询前两天的订单数据
                .where(
                        Criterion.gte("finishDate", date.minusDays(1)),
                        Criterion.lte("finishDate", date),
                        Criterion.in("billType", Arrays.asList("stock_in", "stock_out")),
                        Criterion.eq("status", Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH)
                ).build();
        List<StockBill> stockBills = this.stockBillRepository.findAll(stockBillSpecification);

        List<StockBill> outBill = stockBills.stream().filter(item -> Objects.equals(item.getBillType(), "stock_out")).collect(Collectors.toList());
        List<StockBill> inBill = stockBills.stream().filter(item -> Objects.equals(item.getBillType(), "stock_in")).collect(Collectors.toList());

        List<Long> longIds = prodIds.stream().map(item -> {
            Long value = null;
            try {
                value = Long.valueOf(item);
            } catch (NumberFormatException e) {
            }
            return value;
        }).collect(Collectors.toList());

        List<List<ApplyChainDto>> outChainDto = this.coverBill(outBill, longIds, "出库");
        List<List<ApplyChainDto>> inChainDto = this.coverBill(inBill, longIds, "入库");
        List<List<ApplyChainDto>> result = new ArrayList<>();
        for (List<ApplyChainDto> applyChainDtos : outChainDto) {
            for (List<ApplyChainDto> chainDtos : inChainDto) {
                List<Long> chainProdDtos = !CollectionUtils.isEmpty(applyChainDtos) && applyChainDtos.get(0).getProdDtoList() != null ? applyChainDtos.get(0).getProdDtoList().stream().map(ApplyChainProdDto::getId).collect(Collectors.toList()) : Collections.emptyList();
                List<Long> chainOutProdDto = !CollectionUtils.isEmpty(chainDtos) && chainDtos.get(0).getProdDtoList() != null ? chainDtos.get(0).getProdDtoList().stream().map(ApplyChainProdDto::getId).collect(Collectors.toList()) : Collections.emptyList();
                Boolean have = chainProdDtos.stream().anyMatch(chainOutProdDto::contains);
                if (!have) {
                    continue;
                }
                List<ApplyChainDto> sum = new ArrayList<>(applyChainDtos.size() + chainDtos.size());
                sum.addAll(applyChainDtos);
                sum.addAll(chainDtos);
                result.add(sum);
            }
        }
        return result;
    }

    private List<List<ApplyChainDto>> coverBill(List<StockBill> bills, List<Long> prodIds, String inOrOut) {
        List<List<ApplyChainDto>> outChainDto = new ArrayList<>();
        for (StockBill stockBill : bills) {
            //订单不包含已有商品直接跳过
            if (stockBill.getDetails().stream().noneMatch(item -> item.getGoods() != null && prodIds.contains(item.getGoods().getId()))) {
                continue;
            }
            List<ApplyChainProdDto> chainProdDtos = stockBill.getDetails().stream().filter(item -> item.getGoods() != null && prodIds.contains(item.getGoods().getId())).map(item -> {
                StockGoods stockGoods = item.getGoods();
                return new ApplyChainProdDto()
                        .setId(stockGoods.getId())
                        .setName(stockGoods.getName());
            }).collect(Collectors.toList());

            List<ApplyChainDto> applyChainDtos = new ArrayList<>();
            List<StockBillOperateLog> operateLog = operateLogRepository.findByBillNoAll(stockBill.getId());
            for (StockBillOperateLog stockBillOperateLog : operateLog) {
                ApplyChainDto chainDto = new ApplyChainDto()
                        .setCreateName(stockBillOperateLog.getOptUserName())
                        .setOperation(this.changeTypeName(stockBillOperateLog.getOptType(), inOrOut))
                        .setOperationDate(stockBillOperateLog.getCreateTime())
                        .setProdDtoList(chainProdDtos);
                FeignEmployeeDTO feignEmployeeDTO = this.feignEmployeeService.findById(stockBillOperateLog.getOptUserId());
                if(feignEmployeeDTO != null){
                    chainDto.setCreateAvatar(feignEmployeeDTO.getAvatar());
                }
                chainDto.setSupplier(stockBill.getSupplier() != null ? stockBill.getSupplier().getName() : null);
                applyChainDtos.add(chainDto);
            }

            outChainDto.add(applyChainDtos);
        }
        return outChainDto;
    }

    private String changeTypeName(String key, String inOrOut) {
        switch (key) {
            case "apply":
                return "申请" + inOrOut;
            case "audit":
                return "审核" + inOrOut;
            case "acceptance":
                return "经办" + inOrOut;
            default:
                return "未知操作";
        }
    }
}
