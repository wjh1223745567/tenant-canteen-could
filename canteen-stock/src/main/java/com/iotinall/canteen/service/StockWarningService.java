package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.stock.StockWarningDTO;
import com.iotinall.canteen.dto.stock.StockWarningDetailDTO;
import com.iotinall.canteen.dto.stock.StockWarningQueryReq;
import com.iotinall.canteen.entity.StockDetail;
import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.entity.StockWarning;
import com.iotinall.canteen.repository.StockDetailRepository;
import com.iotinall.canteen.repository.StockGoodsRepository;
import com.iotinall.canteen.repository.StockWarningRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 库存预警处理类
 *
 * @author loki
 * @date 2020/09/09 19:25
 */
@Slf4j
@Service
public class StockWarningService {
    @Autowired
    private StockWarningRepository stockWarningRepository;
    @Autowired
    private StockDetailRepository stockDetailRepository;
    @Autowired
    private StockGoodsRepository stockGoodsRepository;
    @Autowired
    private StockGoodsTypeService goodsTypeService;
    @Autowired
    private StockWarehouseService warehouseService;

    /**
     * 库存预警列表
     *
     * @author loki
     * @date 2020/09/09 19:26
     */
    public Object warningList(StockWarningQueryReq req, Pageable page) {
        //获取类别所有的子类别
        Set<Long> goodsTypeIdList = this.goodsTypeService.getGoodsTypeChildIds(req.getGoodsTypeId());

        //获取所有的子仓库/位置ID
        Set<Long> warehouseIdList = warehouseService.getWarehouseAllChildIds(req.getWarehouseId());

        SpecificationBuilder builder = SpecificationBuilder.builder();
        if (null != req.getWarningType()) {
            if (req.getWarningType() == 1) {
                builder.where(Criterion.eq("shelfLifeWarning", true));
            } else {
                builder.where(Criterion.eq("lowerLimitWarning", true));
            }
        } else {
            builder.whereByOr(Criterion.eq("lowerLimitWarning", true),
                    Criterion.eq("shelfLifeWarning", true));
        }

        builder.where(Criterion.in("goods.type.id", goodsTypeIdList))
                .where(Criterion.in("goods.warehouse.id", warehouseIdList))
                .where(Criterion.like("goods.name", req.getKeyword()));

        Page<StockWarning> result = this.stockWarningRepository.findAll(builder.build(), page);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<StockWarningDTO> warningList = new ArrayList<>();
        StockWarningDTO warningDTO;

        for (StockWarning warning : result.getContent()) {
            warningDTO = new StockWarningDTO();
            warningList.add(warningDTO);
            warningDTO.setId(warning.getId());
            warningDTO.setShelfLifeWarn(warning.getShelfLifeWarning());
            warningDTO.setLowerWarn(warning.getLowerLimitWarning());

            if (null != warning.getGoods()) {
                //获取商品库存
                warningDTO.setAmount(null == warning.getGoods().getStock() ? BigDecimal.ZERO : warning.getGoods().getStock().getAmount());

                if (null != warning.getGoods()) {
                    StockGoods goods = warning.getGoods();
                    warningDTO.setGoodsId(goods.getId());
                    warningDTO.setGoodsCode(goods.getCode());
                    warningDTO.setGoodsName(goods.getName());
                    warningDTO.setGoodsImgUrl(goods.getImgUrl());

                    warningDTO.setUnit(goods.getUnit());
                    warningDTO.setSpecs(goods.getSpecs());
                    warningDTO.setGoodsImgUrl(goods.getImgUrl());
                    warningDTO.setLowerLimit(goods.getLowerLimit());

                    if (null != goods.getType()) {
                        warningDTO.setTypeName(goods.getType().getName());
                    }

                    if (null != goods.getWarehouse()) {
                        warningDTO.setWarehouseName(goods.getWarehouse().getFullName());
                        warningDTO.setWarehouseNameImgUrl(goods.getWarehouse().getImgUrl());
                    }
                }
            }
        }

        return PageUtil.toPageDTO(warningList, result);
    }

    /**
     * 获取预警详情
     *
     * @author loki
     * @date 2020/09/09 20:12
     */
    public Object getWarningDetail(Long id) {
        StockWarning warning = this.stockWarningRepository.findById(id).orElse(null);
        if (null == warning) {
            throw new BizException("", "预警信息不存在");
        }

        //获取商品库存
        if (null == warning.getGoods().getStock()) {
            return null;
        }

        List<StockDetail> stockDetailList = stockDetailRepository.findGoodsLeftStock(warning.getGoods().getId());
        List<StockWarningDetailDTO> detailList = new ArrayList<>();
        StockWarningDetailDTO detail;
        for (StockDetail d : stockDetailList) {
            detail = new StockWarningDetailDTO();
            detailList.add(detail);
            detail.setAmount(d.getAmount());
            detail.setBillDate(d.getBillDetail().getBill().getBillDate());
            detail.setBillNo(d.getBillDetail().getBill().getBillNo());
            detail.setProductionDate(d.getBillDetail().getProductionDate());
            detail.setShelfLife(d.getBillDetail().getShelfLifeDate());
        }

        return detailList;
    }

    /**
     * 更新库存触发库存下线预警计算
     *
     * @author loki
     * @date 2020/09/11 15:45
     */
    public void decisionWarning(StockGoods goods) {
        if (goods.getStock() == null) {
            return;
        }

        StockWarning stockWarning = this.stockWarningRepository.findByGoods(goods);
        if (null == stockWarning) {
            stockWarning = new StockWarning();
            stockWarning.setLowerLimitWarning(false);
            stockWarning.setShelfLifeWarning(false);
            stockWarning.setGoods(goods);
            stockWarning.setCreateTime(LocalDateTime.now());
            stockWarning.setUpdateTime(LocalDateTime.now());
        }

        //库存下线预警
        BigDecimal limit = null == goods.getLowerLimit() ? BigDecimal.ZERO : goods.getLowerLimit();
        stockWarning.setLowerLimitWarning(goods.getStock().getAmount().compareTo(limit) <= 0);

        //获取库存明细
        Integer count = this.stockDetailRepository.countExpiredStock(goods.getId());
        stockWarning.setShelfLifeWarning(count != null && count > 0);

        this.stockWarningRepository.save(stockWarning);
    }

    /**
     * 更新库存处罚库存下线预警计算
     *
     * @author loki
     * @date 2020/09/11 15:45
     */
    public void decisionWarning() {
        List<StockGoods> stockGoodsList = this.stockGoodsRepository.findAll();
        if (CollectionUtils.isEmpty(stockGoodsList)) {
            return;
        }

        for (StockGoods goods : stockGoodsList) {
            this.decisionWarning(goods);
        }
    }
}
