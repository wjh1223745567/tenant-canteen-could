package com.iotinall.canteen.service;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.goods.StockGoodsDTO;
import com.iotinall.canteen.dto.stock.StockDTO;
import com.iotinall.canteen.dto.stock.StockDetailDTO;
import com.iotinall.canteen.dto.stock.StockQueryReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseDTO;
import com.iotinall.canteen.entity.Stock;
import com.iotinall.canteen.entity.StockDetail;
import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.repository.StockDetailRepository;
import com.iotinall.canteen.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 库存逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 11:30
 */
@Slf4j
@Service
public class StockService {
    @Resource
    private StockRepository stockRepository;
    @Resource
    private StockGoodsTypeService stockGoodsTypeService;
    @Resource
    private StockWarehouseService stockWarehouseService;
    @Resource
    private StockDetailRepository stockDetailRepository;

    /**
     * 初始化库存
     *
     * @author loki
     * @date 2021/06/03 11:35
     */
    public Stock initStock() {
        return initStock(BigDecimal.ZERO);
    }

    /**
     * 初始化库存
     *
     * @author loki
     * @date 2021/06/03 11:35
     */
    public Stock initStock(BigDecimal amount) {
        Stock stock = new Stock();
        stock.setAmount(amount)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());

        return this.stockRepository.save(stock);
    }

    /**
     * 获取库存明细,只返回库存数>0的库存
     *
     * @author loki
     * @date 2020/08/31 20:19
     */
    public Object page(StockQueryReq req, Pageable page) {
        PageRequest pr = PageRequest.of(page.getPageNumber(), page.getPageSize());
        //获取类别所有的子类别
        Set<Long> goodsTypeIdList = null;
        if (null != req.getGoodsTypeId() && req.getGoodsTypeId() != -1) {
            goodsTypeIdList = this.stockGoodsTypeService.getGoodsTypeChildIds(req.getGoodsTypeId());
        }

        //获取所有的子仓库/位置ID
        Set<Long> warehouseIdList = stockWarehouseService.getWarehouseAllChildIds(req.getWarehouseId());

        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.in("goods.warehouse.id", warehouseIdList))
                .where(Criterion.in("goods.type.id", goodsTypeIdList))
                .whereByOr(
                        Criterion.like("goods.code", req.getKeyword()),
                        Criterion.like("goods.name", req.getKeyword())
                );
        if (null != req.getIgnoreZero() && req.getIgnoreZero()) {
            builder.where(Criterion.gt("amount", 0));
        }

        Page<Stock> result = stockRepository.findAll(builder.build(), pr);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<Stock> stockList = result.getContent();
        List<StockDTO> stockDTOList = new ArrayList<>();
        StockDTO stockDTO;
        for (Stock stock : stockList) {
            stockDTO = new StockDTO();
            stockDTOList.add(stockDTO);

            if (null != stock.getGoods()) {
                BeanUtils.copyProperties(stock.getGoods(), stockDTO);
                stockDTO.setGoodsId(stock.getGoods().getId());
                stockDTO.setLowerLimit(stock.getGoods().getLowerLimit());

                if (null != stock.getGoods().getWarehouse()) {
                    stockDTO.setWarehouseId(stock.getGoods().getWarehouse().getId());
                    stockDTO.setWarehouseName(stock.getGoods().getWarehouse().getFullName());
                    stockDTO.setWarehouseImgUrl(stock.getGoods().getWarehouse().getImgUrl());
                }

                if (null != stock.getGoods().getType()) {
                    stockDTO.setGoodsTypeId(stock.getGoods().getType().getId());
                    stockDTO.setGoodsTypeName(stock.getGoods().getType().getName());
                }
            }

            //库存数量
            stockDTO.setAmount(stock.getAmount());
        }
        return PageUtil.toPageDTO(stockDTOList, result);
    }

    /**
     * 获取库存明细,只返回库存数>0的库存
     *
     * @author loki
     * @date 2020/08/31 20:19
     */
    public Object getStockDetailList(StockQueryReq req, Pageable page) {
        //获取类别所有的子类别
        Set<Long> goodsTypeIdList = this.stockGoodsTypeService.getGoodsTypeChildIds(req.getGoodsTypeId());

        Page<StockDetail> result = this.stockDetailRepository.search(req.getBeginDate(),
                req.getEndDate(),
                req.getBillNo(),
                Constants.BILL_STATUS.STOCK_IN_OUT_ORDER_STATUS_FINISH,
                req.getGoodsId(),
                Collections.singleton(req.getWarehouseId()),
                goodsTypeIdList,
                req.getSupplierId(),
                req.getKeyword(),
                page);

        if (result.getTotalElements() <= 0) {
            return PageUtil.toPageDTO(Collections.EMPTY_LIST, 0L);
        }

        List<StockDetail> detailList = result.getContent();
        List<StockDetailDTO> detailDTOList = new ArrayList<>();
        StockDetailDTO detailDTO;
        for (StockDetail detail : detailList) {
            detailDTO = new StockDetailDTO();
            detailDTOList.add(detailDTO);
            BeanUtils.copyProperties(detail.getBillDetail(), detailDTO);
            detailDTO.setBillDetailId(detail.getBillDetail().getId());
            detailDTO.setBillNo(detail.getBillDetail().getBill().getBillNo());
            detailDTO.setAmount(detail.getAmount());

            if (null != detail.getGoods()) {
                StockGoodsDTO goodsDTO = new StockGoodsDTO();
                BeanUtils.copyProperties(detail.getGoods(), goodsDTO);
                detailDTO.setStockGoodsDTO(goodsDTO);

                if (null != detail.getGoods().getType()) {
                    goodsDTO.setGoodsTypeId(detail.getGoods().getType().getId());
                    goodsDTO.setGoodsTypeName(detail.getGoods().getType().getName());
                }

                if (null != detail.getGoods().getWarehouse()) {
                    StockWarehouseDTO warehouseDTO = new StockWarehouseDTO();
                    goodsDTO.setWarehouse(warehouseDTO);
                    BeanUtils.copyProperties(detail.getGoods().getWarehouse(), warehouseDTO);
                }
            }
        }
        return PageUtil.toPageDTO(detailDTOList, result);
    }
}
