package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.bill.StockStatDTO;
import com.iotinall.canteen.dto.bill.StockStatDetailDTO;
import com.iotinall.canteen.dto.bill.StockStatQueryReq;
import com.iotinall.canteen.dto.outbill.OrgStockOutDTO;
import com.iotinall.canteen.dto.outbill.OrgStockOutQueryReq;
import com.iotinall.canteen.dto.stock.*;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.utils.DateTimeFormatters;
import com.iotinall.canteen.utils.LocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 当局统计相关接口
 *
 * @author loki
 * @date 2021/6/8 17:20
 **/
@Service
public class StockBillStatService extends StockBillCommonService {

    /**
     * 库存汇总
     *
     * @author loki
     * @date 2021/6/8 17:21
     **/
    public Object statStock(StockStatQueryReq req, Pageable page) {
        Specification<Stock> spec = SpecificationBuilder.builder()
                .where(Criterion.eq("goods.warehouse.id", req.getWarehouseId()))
                .where(Criterion.eq("goods.type.id", req.getGoodsTypeId()))
                .whereByOr(
                        Criterion.like("goods.name", req.getKeyword())
                )
                .build();
        Page<Stock> result = stockRepository.findAll(spec, page);

        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        //判断查询月份是否为本月，本月需要实时统计库存数，其他读取统计表中数据
        LocalDate currentDate = LocalDate.now();
        boolean isCurrentMonth = req.getMonth().withDayOfMonth(1).compareTo(currentDate.withDayOfMonth(1)) == 0;

        List<Stock> stockList = result.getContent();
        List<StockStatDTO> stockStatList = new ArrayList<>(stockList.size());
        StockStatDTO stockStatDTO;
        for (Stock stock : stockList) {
            stockStatDTO = new StockStatDTO();
            stockStatList.add(stockStatDTO);
            if (null != stock.getGoods()) {
                StockGoods goods = stock.getGoods();
                BeanUtils.copyProperties(goods, stockStatDTO);
                stockStatDTO.setGoodsId(goods.getId());
                if (null != goods.getType()) {
                    stockStatDTO.setGoodsTypeName(goods.getType().getName());
                }
                if (null != goods.getWarehouse()) {
                    stockStatDTO.setWarehouseName(goods.getWarehouse().getFullName());
                }

                if (isCurrentMonth) {
                    this.statCurrentMonthStock(stockStatDTO, req.getMonth(), goods);
                } else {
                    this.statOtherMonthStock(stockStatDTO, req.getMonth(), goods.getId());
                }
            }
        }
        return PageUtil.toPageDTO(stockStatList, result);
    }

    /**
     * 汇总库存
     *
     * @author loki
     * @date 2020/09/14 20:34
     */
    private void statCurrentMonthStock(StockStatDTO stockStatDTO, LocalDate date, StockGoods goods) {
        //统计上月结余
        stockStatDTO.setPreMonthTotal(getStat(date.minusMonths(1), goods.getId(), Constants.STOCK_STAT_TYPE_LEFT));

        /**
         * 本月入库（本月采购入库-采购退货
         */
        //采购入库
        List<StockBillDetail> stockInList = this.stockBillDetailRepository.findDetailList(
                Constants.BILL_TYPE.STOCK_IN,
                date,
                goods.getId(),
                Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH);
        if (!CollectionUtils.isEmpty(stockInList)) {
            stockStatDTO.setCurrentMonthStockIn(groupByPrice(getDetailList(stockInList)));
        }

        /**
         * 本月出库（本月领用出库-本月领用退库）
         */
        List<StockBillDetail> stockOutList = this.stockBillDetailRepository.findDetailList(
                Constants.BILL_TYPE.STOCK_OUT,
                date,
                goods.getId(),
                Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH);
        if (!CollectionUtils.isEmpty(stockOutList)) {
            stockStatDTO.setCurrentMonthStockOut(groupByPrice(getDetailList(stockOutList)));
        }

        /**
         * 本月结存 （本月入库+上月结存）-本月出库
         */
        stockStatDTO.setCurrentMonthTotal(calculateCurrentMonthTotal(
                stockStatDTO.getCurrentMonthStockIn(),
                stockStatDTO.getPreMonthTotal(),
                stockStatDTO.getCurrentMonthStockOut()));
    }

    /**
     * 计算本月结余
     *
     * @author loki
     * @date 2021/6/9 11:46
     **/
    private List<StockStatDetailDTO> calculateCurrentMonthTotal(List<StockStatDetailDTO> currentMonthIn,
                                                                List<StockStatDetailDTO> lastMonthLeft,
                                                                List<StockStatDetailDTO> currentMonthOut) {
        if (CollectionUtils.isEmpty(currentMonthIn)) {
            currentMonthIn = new ArrayList<>();
        }
        currentMonthIn.addAll(lastMonthLeft);
        List<StockStatDetailDTO> result = groupByPrice(currentMonthIn);

        if (CollectionUtils.isEmpty(currentMonthOut)) {
            return result;
        }
        for (StockStatDetailDTO in : result) {
            for (StockStatDetailDTO out : currentMonthOut) {
                if (in.getPrice().equals(out.getPrice())) {
                    in.setCount(in.getCount().subtract(out.getCount()));
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 转换
     *
     * @author loki
     * @date 2021/6/9 11:39
     **/
    private List<StockStatDetailDTO> getDetailList(List<StockBillDetail> stockBillDetailList) {
        List<StockStatDetailDTO> detailList = new ArrayList<>();

        StockStatDetailDTO detail;
        List<StockBillDetail> outBackBillDetailList;
        BigDecimal outBackCount = BigDecimal.ZERO;
        for (StockBillDetail billDetail : stockBillDetailList) {
            detail = new StockStatDetailDTO();
            detailList.add(detail);

            //退库/退货数量
            outBackBillDetailList = this.stockBillDetailRepository.findByBillDetail(billDetail);
            if (!CollectionUtils.isEmpty(outBackBillDetailList)) {
                for (StockBillDetail outBackBillDetail : outBackBillDetailList) {
                    outBackCount = outBackCount.add(outBackBillDetail.getRealAmount());
                }
            }

            if (billDetail.getRealAmount().compareTo(outBackCount) > 0) {
                detail.setCount(billDetail.getRealAmount().subtract(outBackCount));
                detail.setPrice(billDetail.getPrice());
            }
        }

        return groupByPrice(detailList);
    }

    /**
     * 非当月库存汇总
     *
     * @author loki
     * @date 2020/09/14 20:34
     */
    private void statOtherMonthStock(StockStatDTO stockStatDTO, LocalDate date, Long goodsId) {
        //统计上月结余
        stockStatDTO.setPreMonthTotal(getStat(date.minusMonths(1), goodsId, Constants.STOCK_STAT_TYPE_LEFT));
        //本月入库
        stockStatDTO.setCurrentMonthStockIn(getStat(date, goodsId, Constants.STOCK_STAT_TYPE_STOCK_IN));
        //本月出库
        stockStatDTO.setCurrentMonthStockIn(getStat(date, goodsId, Constants.STOCK_STAT_TYPE_STOCK_OUT));
    }

    private List<StockStatDetailDTO> getStat(LocalDate date, Long goodsId, int type) {
        List<StockStat> statList = this.stockStatRepository.findGoodsStockStat(
                date,
                goodsId,
                type);
        if (!CollectionUtils.isEmpty(statList)) {
            List<StockStatDetailDTO> detailList = new ArrayList<>();
            StockStatDetailDTO detail;

            for (StockStat stat : statList) {
                detail = new StockStatDetailDTO();
                detailList.add(detail);
                detail.setCount(stat.getCount());
                detail.setPrice(stat.getPrice());
            }

            return groupByPrice(detailList);
        }

        return Collections.EMPTY_LIST;
    }

    /**
     * 根据价格分组
     *
     * @author loki
     * @date 2020/09/15 11:50
     */
    private List<StockStatDetailDTO> groupByPrice(List<StockStatDetailDTO> detailList) {
        if (CollectionUtils.isEmpty(detailList)) {
            return Collections.EMPTY_LIST;
        }

        Map<BigDecimal, List<StockStatDetailDTO>> detailGroupList = detailList
                .stream()
                .collect(Collectors.groupingBy(StockStatDetailDTO::getPrice));

        List<StockStatDetailDTO> result = new ArrayList<>();
        StockStatDetailDTO detailDTO;
        for (Map.Entry<BigDecimal, List<StockStatDetailDTO>> r : detailGroupList.entrySet()) {
            detailDTO = new StockStatDetailDTO();
            result.add(detailDTO);
            detailDTO.setPrice(r.getKey());
            detailDTO.setCount(r.getValue().stream().map(StockStatDetailDTO::getCount).reduce(BigDecimal.ZERO, BigDecimal::add));
            detailDTO.setAmount(detailDTO.getPrice().multiply(detailDTO.getCount()));
        }

        return result;
    }

    /**
     * 部门出库汇总
     *
     * @author loki
     * @date 2021/02/01 15:24
     */
    public Object statStockInOutDetail(OrgStockOutQueryReq req, Pageable page) {
        //获取所有的子部门,只有出库单有部门
        Set<Long> orgIdList = null;
        if (null != req.getOrgId()) {
            orgIdList = feignOrgService.getAllChildOrg(Collections.singletonList(req.getOrgId()));
        }

        Page<OrgStockOutDTO> result = this.stockBillDetailRepository.statStockInOutDetail(
                orgIdList,
                Collections.singleton(req.getBillType()),
                req.getSupplierId(),
                req.getKeywords(),
                req.getBeginDate(),
                req.getEndDate(),
                page);

        return PageUtil.toPageDTO(result);
    }

    /**
     * 食堂大屏，仓库实况
     *
     * @author loki
     * @date 2021/7/13 10:15
     **/
    public FeignTodayStockMoneyDTO getTodayStockMoney() {
        return new FeignTodayStockMoneyDTO()
                .setInAmountToday(this.stockStatRepository.statStockAmount(Constants.BILL_TYPE.STOCK_IN, LocalDate.now()))
                .setOutAmountToday(this.stockStatRepository.statStockAmount(Constants.BILL_TYPE.STOCK_OUT, LocalDate.now()));
    }

    /**
     * 食堂大屏，近30天出入库金额统计
     *
     * @author loki
     * @date 2021/7/13 10:33
     **/
    public List<FeignStat30StockInOutMoney> getStockMoney30() {
        LocalDate end = LocalDate.now();
        LocalDate begin = end.minusDays(29);
        List<LocalDateUtil.BetweenDate> betweenDateList = LocalDateUtil.getAllBetweenDate(begin, end);

        List<StockInOutMoneyStatDTO> statList = stockStatRepository.statStockMoney(begin, end);

        List<FeignStat30StockInOutMoney> result = new ArrayList<>(30);
        FeignStat30StockInOutMoney data;
        for (LocalDateUtil.BetweenDate betweenDate : betweenDateList) {
            data = new FeignStat30StockInOutMoney();
            result.add(data);
            data.setDate(betweenDate.getLabel());
            List<StockInOutMoneyStatDTO> dayStat = statList
                    .stream()
                    .filter(item -> item.getDate().equals(LocalDateUtil.format(betweenDate.getDate(), DateTimeFormatters.STANDARD_DATE_FORMATTER)))
                    .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(dayStat)) {
                for (StockInOutMoneyStatDTO item : dayStat) {
                    if (item.getBillType().equals(Constants.BILL_TYPE.STOCK_IN)) {
                        data.setInAmount(item.getMoney());
                    }

                    if (item.getBillType().equals(Constants.BILL_TYPE.STOCK_OUT)) {
                        data.setOutAmount(item.getMoney());
                    }
                }
            }
        }

        return result;
    }

    public List<StatStockInTop5> getStockInTop5() {
        Page<Stock> result = stockRepository.findTop5(PageRequest.of(0, 5));
        if (CollectionUtils.isEmpty(result.getContent())) {
            return Collections.EMPTY_LIST;
        }

        List<StatStockInTop5> top5List = new ArrayList<>(5);
        StatStockInTop5 top5;
        for (Stock stock : result.getContent()) {
            if (stock.getGoods() == null) {
                continue;
            }
            top5 = new StatStockInTop5();
            top5List.add(top5);

            top5.setGoodsName(stock.getGoods().getName());
            top5.setUnit(stock.getGoods().getUnit());
            top5.setStockAmount(stock.getAmount());
        }
        return top5List;
    }

    public List<StatStockWarningDTO> getStockWarning() {
        SpecificationBuilder builder = SpecificationBuilder.builder().whereByOr(Criterion.eq("lowerLimitWarning", true),
                Criterion.eq("shelfLifeWarning", true));

        Page<StockWarning> result = stockWarningRepository.findAll(builder.build(), PageRequest.of(0, 10));
        if (CollectionUtils.isEmpty(result.getContent())) {
            return Collections.EMPTY_LIST;
        }

        List<StatStockWarningDTO> warningList = new ArrayList<>(10);
        StatStockWarningDTO warning;
        Stock stock;
        for (StockWarning stockWarning : result.getContent()) {
            if (null == stockWarning.getGoods()) {
                continue;
            }
            warning = new StatStockWarningDTO();
            warningList.add(warning);
            warning.setGoodsName(stockWarning.getGoods().getName());

            //获取商品库存
            stock = stockWarning.getGoods().getStock();
            warning.setStockAmount(null == stock ? BigDecimal.ZERO : stock.getAmount());
            warning.setUnit(stockWarning.getGoods().getUnit());
        }
        return warningList;
    }

    public List<StockInCostDTO> getStockInTotalMoneyByType() {
        //获取一级商品类型分类
        List<StockGoodsType> goodsTypeList = stockGoodsTypeRepository.findByPid(-1L);
        if (!CollectionUtils.isEmpty(goodsTypeList)) {
            List<StockInCostDTO> result = new ArrayList<>();
            StockInCostDTO cost;

            Set<Long> typeIds;
            LocalDate end = LocalDate.now();
            LocalDate begin = end.minusDays(29);
            BigDecimal amount;
            for (StockGoodsType type : goodsTypeList) {
                cost = new StockInCostDTO();
                result.add(cost);
                cost.setTypeId(type.getId());
                cost.setTypeName(type.getName());

                //获取该类型下面所有的子类型
                typeIds = stockGoodsTypeService.getGoodsTypeChildIds(type.getId());
                amount = stockStatRepository.statStockAmountByType(begin, end, typeIds, Constants.BILL_TYPE.STOCK_IN);
                cost.setAmount(null == amount ? BigDecimal.ZERO : amount);
            }

            result = result.stream().sorted(Comparator.comparing(StockInCostDTO::getAmount).reversed()).collect(Collectors.toList());

            if (result.size() > 5) {
                List<StockInCostDTO> top5 = result.subList(0, 5);
                List<StockInCostDTO> other = result.subList(6, result.size());
                BigDecimal otherTotalAmount = BigDecimal.ZERO;
                for (StockInCostDTO o : other) {
                    otherTotalAmount = otherTotalAmount.add(o.getAmount());
                }

                StockInCostDTO r = new StockInCostDTO();
                r.setTypeName("其他");
                r.setAmount(otherTotalAmount);
                top5.add(r);
                result = top5;
            }

            return result;
        }

        return null;
    }

    public List<StockAcceptImgAndReportImg> getBillAcceptImgAndReportImg() {
        List<StockBill> stockBillList = this.stockBillRepository.findByBillDateAndBillType(LocalDate.now(), Constants.BILL_TYPE.STOCK_IN);
        if (!CollectionUtils.isEmpty(stockBillList)) {
            StockAcceptImgAndReportImg stockInImg;
            List<StockAcceptImgAndReportImg> result = new ArrayList<>();
            for (StockBill bill : stockBillList) {
                //验收凭证
                for (StockBillDetail detail : bill.getDetails()) {
                    if (detail.getAcceptance() && StringUtils.isNotBlank(detail.getImgUrl())) {
                        stockInImg = new StockAcceptImgAndReportImg();
                        result.add(stockInImg);
                        stockInImg.setName(detail.getGoods().getName());
                        stockInImg.setImgUrl(ImgPair.getFileServer() + detail.getImgUrl());
                        stockInImg.setUpdateTime(detail.getUpdateTime());
                    }
                }

                //检测报告
                List<StockCertificateAndInspectionReport> stockBillImgList = stockCertificateAndInspectionReportRepository.findByStockBill(bill);
                for (StockCertificateAndInspectionReport img : stockBillImgList) {
                    if (StringUtils.isBlank(img.getImgUrl())) {
                        continue;
                    }
                    stockInImg = new StockAcceptImgAndReportImg();
                    result.add(stockInImg);
                    stockInImg.setName("检测报告");
                    stockInImg.setImgUrl(StringUtils.isNotBlank(img.getImgUrl()) ? ImgPair.getFileServer() + img.getImgUrl() : "");
                    stockInImg.setUpdateTime(img.getCreateTime());
                }
            }

            result = result.stream().sorted(Comparator.comparing(StockAcceptImgAndReportImg::getUpdateTime).reversed()).collect(Collectors.toList());
            return result;
        }
        return null;
    }
}
