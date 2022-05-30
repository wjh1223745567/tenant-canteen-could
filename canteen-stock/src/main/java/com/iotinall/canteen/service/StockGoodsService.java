package com.iotinall.canteen.service;

import com.alibaba.excel.EasyExcel;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.goods.StockGoodsAddReq;
import com.iotinall.canteen.dto.goods.StockGoodsDTO;
import com.iotinall.canteen.dto.goods.StockGoodsQueryReq;
import com.iotinall.canteen.dto.goods.StockGoodsUpdateReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseDTO;
import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.entity.StockGoodsType;
import com.iotinall.canteen.entity.StockWarehouse;
import com.iotinall.canteen.excel.goods.StockGoodsExcelDTO;
import com.iotinall.canteen.excel.goods.StockGoodsExcelListener;
import com.iotinall.canteen.repository.*;
import com.iotinall.canteen.dto.stock.FeignStockProdDto;
import com.iotinall.canteen.utils.CodeGeneratorUtil;
import com.iotinall.canteen.utils.PinyinUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品逻辑处理类
 *
 * @author loki
 * @date 2021/06/02 20:41
 */
@Slf4j
@Service
public class StockGoodsService {
    @Resource
    private StockGoodsRepository stockGoodsRepository;
    @Resource
    private StockGoodsTypeService stockGoodsTypeService;
    @Resource
    private StockWarehouseService stockWarehouseService;
    @Resource
    private StockGoodsTypeRepository stockGoodsTypeRepository;
    @Resource
    private StockWarehouseRepository stockWarehouseRepository;
    @Resource
    private StockService stockService;
    @Resource
    private StockRepository stockRepository;
    @Resource
    private StockBillDetailRepository stockBillDetailRepository;

    /**
     * 查询货品列表
     */
    public Object page(StockGoodsQueryReq req, Pageable pageable) {
        //获取类别所有的子类别
        Set<Long> goodsTypeIdList = this.stockGoodsTypeService.getGoodsTypeChildIds(req.getGoodsTypeId());

        //获取所有的子仓库
        Set<Long> warehouseIdList = stockWarehouseService.getWarehouseChildIds(req.getWarehouseId());

        Specification<StockGoods> spec = SpecificationBuilder.builder()
                .where(Criterion.in("type.id", goodsTypeIdList))
                .where(Criterion.in("warehouse.id", warehouseIdList))
                .whereByOr(
                        Criterion.like("name", req.getKeyword()),
                        Criterion.like("code", req.getKeyword())
                ).build();
        Page<StockGoods> result = stockGoodsRepository.findAll(spec, pageable);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<StockGoods> stockGoodsList = result.getContent();
        List<StockGoodsDTO> stockGoodsDTOList = new ArrayList<>();
        StockGoodsDTO stockGoodsDTO;
        for (StockGoods stockGoods : stockGoodsList) {
            stockGoodsDTO = new StockGoodsDTO();
            stockGoodsDTOList.add(stockGoodsDTO);
            BeanUtils.copyProperties(stockGoods, stockGoodsDTO);

            if (stockGoods.getWarehouse() != null) {
                StockWarehouseDTO warehouseDTO = new StockWarehouseDTO();
                BeanUtils.copyProperties(stockGoods.getWarehouse(), warehouseDTO);
                stockGoodsDTO.setWarehouse(warehouseDTO);
            }

            if (stockGoods.getType() != null) {
                stockGoodsDTO.setGoodsTypeId(stockGoods.getType().getId());
                stockGoodsDTO.setGoodsTypeName(stockGoods.getType().getName());
            }

            //库存
            stockGoodsDTO.setStockAmount(null == stockGoods.getStock() ? BigDecimal.ZERO : stockGoods.getStock().getAmount());
        }
        return PageUtil.toPageDTO(stockGoodsDTOList, result);
    }

    /**
     * excel导入商品
     *
     * @author loki
     * @date 2021/06/03 11:18
     */
    public void excelAddGoods(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), StockGoodsExcelDTO.class,
                    new StockGoodsExcelListener()).sheet().headRowNumber(1).autoTrim(true).doRead();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }

    /**
     * excel导入商品保存
     *
     * @author loki
     * @date 2021/06/03 11:20
     */
    public void importGoods(List<StockGoodsExcelDTO> goodsList) {
        for (StockGoodsExcelDTO goodsDTO : goodsList) {
            StockGoods stockGoods = new StockGoods();
            BigDecimal price = new BigDecimal(goodsDTO.getPrice());

            StockGoodsType rootType = this.stockGoodsTypeRepository.findById(-1L).orElse(null);
            if (null == rootType) {
                rootType = new StockGoodsType();
                rootType.setId(-1L);
                rootType.setName("商品类别");
                rootType.setCode("T0001");
                rootType.setCreateTime(LocalDateTime.now());
                rootType.setUpdateTime(LocalDateTime.now());
                this.stockGoodsTypeRepository.saveAndFlush(rootType);
            }

            StockGoodsType goodsType = this.stockGoodsTypeRepository.findByName(goodsDTO.getGoodsType());

            if (null == goodsType) {
                goodsType = new StockGoodsType();
                goodsType.setName(goodsDTO.getGoodsType());
                goodsType.setCode(CodeGeneratorUtil.buildGoodsTypeCode());
                goodsType.setPid(-1L);
                goodsType.setCreateTime(LocalDateTime.now());
                goodsType.setUpdateTime(LocalDateTime.now());
                this.stockGoodsTypeRepository.save(goodsType);
            }

            StockGoods goods = this.stockGoodsRepository.findByName(goodsDTO.getName());
            if (goods != null) {
                if (null == goods.getStock()) {
                    goods.setStock(stockService.initStock());
                }

                goods.setPrice(price);
                goods.setNickname(goodsDTO.getNickname());
                goods.setType(goodsType);
                this.stockGoodsRepository.save(goods);
                log.info("重复商品:" + goods.getName());
                continue;
            }

            stockGoods.setCode(goodsDTO.getCode());
            stockGoods.setName(goodsDTO.getName());
            stockGoods.setNickname(goodsDTO.getNickname());
            stockGoods.setSpecs(goodsDTO.getSpecs());
            stockGoods.setUnit(goodsDTO.getUnit());
            stockGoods.setPrice(price);
            stockGoods.setType(goodsType);
            stockGoods.setLowerLimit(BigDecimal.valueOf(5));
            stockGoods.setCreateTime(LocalDateTime.now());
            stockGoods.setUpdateTime(LocalDateTime.now());
            stockGoods.setStock(stockService.initStock());
            stockGoods.setNameFirstLetter(PinyinUtil.toFirstLetter(goodsDTO.getName()));
            stockGoods.setNamePinYin(PinyinUtil.toFullPinyin(goodsDTO.getName()));

            this.stockGoodsRepository.saveAndFlush(stockGoods);
        }
    }

    /**
     * 添加商品
     *
     * @author loki
     * @date 2021/06/03 14:07
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(StockGoodsAddReq req) {
        //校验名称唯一性
        StockGoods stockGoods = this.stockGoodsRepository.findByName(req.getName());
        if (null != stockGoods) {
            throw new BizException("", "商品名称已存在");
        }

        StockGoodsType goodsType = stockGoodsTypeRepository.findById(req.getGoodsTypeId())
                .orElseThrow(() -> new BizException("商品类别不存在"));

        Optional<StockWarehouse> warehouseById = stockWarehouseRepository.findById(req.getWarehouseId());
        if (!warehouseById.isPresent()) {
            throw new BizException("", "仓库不存在");
        }

        stockGoods = this.stockGoodsRepository.findByCode(req.getCode());
        if (null != stockGoods) {
            throw new BizException("", "货品编号已存在");
        }
        stockGoods = new StockGoods();
        BeanUtils.copyProperties(req, stockGoods);
        stockGoods.setWarehouse(warehouseById.get());
        stockGoods.setType(goodsType);
        stockGoods.setCreateTime(LocalDateTime.now());
        stockGoods.setUpdateTime(LocalDateTime.now());
        stockGoods.setNickname(req.getNickname());
        stockGoods.setPrice(req.getPrice());
        stockGoods.setNamePinYin(PinyinUtil.toFullPinyin(stockGoods.getName()));
        stockGoods.setNameFirstLetter(PinyinUtil.toFirstLetter(stockGoods.getName()));
        this.stockGoodsRepository.save(stockGoods);
    }

    /**
     * 编辑货品
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StockGoodsUpdateReq req) {
        StockGoods stockGoods = stockGoodsRepository.findById(req.getId()).orElseThrow(
                () ->
                        new BizException("商品不存在")
        );

        LocalDateTime now = LocalDateTime.now();
        StockGoodsType goodsType = stockGoodsTypeRepository.findById(req.getGoodsTypeId()).orElseThrow(
                () -> new BizException("货品类别不存在")
        );

        StockWarehouse warehouse = stockWarehouseRepository.findById(req.getWarehouseId()).orElseThrow(
                () -> new BizException("仓库不存在")
        );

        StockGoods stockGoodsName = stockGoodsRepository.findByName(req.getName());
        if (null != stockGoodsName && !stockGoodsName.getId().equals(req.getId())) {
            throw new BizException("", "商品名称已存在");
        }

        StockGoods stockGoodsCode = stockGoodsRepository.findByCode(req.getCode());
        if (null != stockGoodsCode && !stockGoodsCode.getId().equals(req.getId())) {
            throw new BizException("", "商品编号已存在");
        }

        BeanUtils.copyProperties(req, stockGoods);
        stockGoods.setUpdateTime(now);
        stockGoods.setType(goodsType);
        stockGoods.setWarehouse(warehouse);
        stockGoods.setNickname(req.getNickname());
        stockGoods.setPrice(req.getPrice());
        stockGoods.setNamePinYin(PinyinUtil.toFullPinyin(stockGoods.getName()));
        stockGoods.setNameFirstLetter(PinyinUtil.toFirstLetter(stockGoods.getName()));
        this.stockGoodsRepository.save(stockGoods);
    }

    /**
     * 删除商品
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的商品");
        }

        Long count;
        for (Long id : ids) {
            StockGoods goods = this.stockGoodsRepository.findById(id).orElse(null);
            if (null == goods) {
                throw new BizException("", "商品不存在");
            }

            //校验商品是否被使用
            count = this.stockBillDetailRepository.countByGoods(goods);
            if (0 != count) {
                throw new BizException("", "商品【" + goods.getName() + "】已被使用，不能删除");
            }

            //校验商品是否被使用
            if (null != goods.getStock()) {
                if (goods.getStock().getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    throw new BizException("", "商品【" + goods.getName() + "】库存不为0，不能删除");
                }
                this.stockRepository.delete(goods.getStock());
            }

            this.stockGoodsRepository.delete(goods);
        }
    }

    /**
     * 出/入库明细商品显示
     *
     * @param goodsId
     * @return
     */
    public StockGoodsDTO findByGoodsId(Long goodsId) {
        StockGoods goods = this.stockGoodsRepository.findById(goodsId).orElseThrow(
                () -> new BizException("商品不存在")
        );

        StockGoodsDTO goodsDTO = new StockGoodsDTO();
        BeanUtils.copyProperties(goods, goodsDTO);
        goodsDTO.setCode(goods.getCode());
        goodsDTO.setName(goods.getName());
        goodsDTO.setGoodsTypeName(goods.getType().getName());
        goodsDTO.setSpecs(goods.getSpecs());
        goodsDTO.setPrice(goods.getPrice());

        if (null != goods.getWarehouse()) {
            StockWarehouseDTO warehouseDTO = new StockWarehouseDTO();
            BeanUtils.copyProperties(goods.getWarehouse(), warehouseDTO);
            goodsDTO.setWarehouse(warehouseDTO);
        }
        return goodsDTO;
    }

    /**
     * 中文名称转拼音
     *
     * @author loki
     * @date 2021/05/31 10:24
     */
    public void convert2Pinyin() {
        List<StockGoods> goodsList = this.stockGoodsRepository.findAll();
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }

        String fullPinYin;
        String firstLetter;
        for (StockGoods goods : goodsList) {
            if (StringUtils.isBlank(goods.getName())) {
                continue;
            }

            fullPinYin = PinyinUtil.toFullPinyin(goods.getName());
            firstLetter = PinyinUtil.toFirstLetter(goods.getName());
            goods.setNamePinYin(fullPinYin);
            goods.setNameFirstLetter(firstLetter);
            this.stockGoodsRepository.save(goods);
            log.info("中文：{}，拼音：{},首字母：{}", goods.getName(), fullPinYin, firstLetter);
        }
    }

    public List<FeignStockProdDto> findTop10Material(String key, Long typeId){
        SpecificationBuilder stockGoodsSpecification = SpecificationBuilder.builder()
                .where(
                        Criterion.like("name", key)
                );
        if(typeId != null){
            stockGoodsSpecification.where(Criterion.eq("type.id", typeId));
        }
        Specification<StockGoods> specification = stockGoodsSpecification.build();

        Page<StockGoods> page = stockGoodsRepository.findAll(specification, PageRequest.of(0, 10));
        return page.get().map(item -> {
            FeignStockProdDto materialDTO = new FeignStockProdDto()
                    .setId(item.getId())
                    .setQuality(item.getQuality())
                    .setName(item.getName());
            return materialDTO;
        }).collect(Collectors.toList());
    }
}
