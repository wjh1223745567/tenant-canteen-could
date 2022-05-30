package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.goodstype.GoodsTypeAddReq;
import com.iotinall.canteen.dto.goodstype.GoodsTypeTreeDTO;
import com.iotinall.canteen.dto.goodstype.GoodsTypeUpdateReq;
import com.iotinall.canteen.dto.goodstype.StockGoodsTypeDTO;
import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.entity.StockGoodsType;
import com.iotinall.canteen.repository.StockGoodsRepository;
import com.iotinall.canteen.repository.StockGoodsTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品类型逻辑处理类
 *
 * @author loki
 * @date 2021/06/02 20:41
 */
@Slf4j
@Service
public class StockGoodsTypeService {
    @Resource
    private StockGoodsTypeRepository stockGoodsTypeRepository;
    @Resource
    private StockGoodsRepository stockGoodsRepository;

    /**
     * 商品类别树
     *
     * @return
     */
    public Object tree() {
        List<StockGoodsType> result = stockGoodsTypeRepository.findTree();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return result.stream().map(
                this::convertToTree).collect(Collectors.toList());
    }


    /**
     * 将LIST转成树
     *
     * @param goodsType
     * @return
     */
    private GoodsTypeTreeDTO convertToTree(StockGoodsType goodsType) {
        GoodsTypeTreeDTO treeDTO = new GoodsTypeTreeDTO()
                .setId(goodsType.getId())
                .setName(goodsType.getName())
                .setParentId(goodsType.getPid());

        Integer count = stockGoodsRepository.countByStockGoodsTypeId(this.getGoodsTypeChildIds(goodsType));
        treeDTO.setGoodsCount(null == count ? 0 : count);

        if (!CollectionUtils.isEmpty(goodsType.getChildren())) {
            treeDTO.setChildren(goodsType.getChildren().stream().map(this::convertToTree).collect(Collectors.toList()));
        }

        return treeDTO;
    }

    /**
     * 获取所有商品类型
     *
     * @author loki
     * @date 2021/06/03 10:39
     */
    public Object all() {
        List<StockGoodsType> stockGoodsTypes = stockGoodsTypeRepository.findAll();
        if (CollectionUtils.isEmpty(stockGoodsTypes)) {
            return Collections.EMPTY_LIST;
        }

        List<StockGoodsTypeDTO> goodsTypeDTOList = new ArrayList<>();
        StockGoodsTypeDTO stockGoodsTypeDTO;
        for (StockGoodsType goodsType : stockGoodsTypes) {
            stockGoodsTypeDTO = new StockGoodsTypeDTO();
            goodsTypeDTOList.add(stockGoodsTypeDTO);
            BeanUtils.copyProperties(goodsType, stockGoodsTypeDTO);
        }

        return goodsTypeDTOList;
    }

    /**
     * 获取所有子商品类型ID，包括自己
     *
     * @author loki
     * @date 2021/06/03 10:16
     */
    public Set<Long> getGoodsTypeChildIds(Long id) {
        if (null == id) {
            return Collections.EMPTY_SET;
        }
        StockGoodsType goodsType = this.stockGoodsTypeRepository.findById(id).orElseThrow(
                () -> new BizException("商品类别不存在")
        );

        return getGoodsTypeChildIds(goodsType);
    }

    /**
     * 获取所有子商品类型ID，包括自己
     *
     * @author loki
     * @date 2021/06/03 10:16
     */
    public Set<Long> getGoodsTypeChildIds(StockGoodsType goodsType) {
        Set<Long> ids = new HashSet<>();
        ids.add(goodsType.getId());
        if (!CollectionUtils.isEmpty(goodsType.getChildren())) {
            Set<StockGoodsType> children = goodsType.getChildren();
            for (StockGoodsType child : children) {
                ids.addAll(getGoodsTypeChildIds(child));
            }
        }
        return ids;
    }

    /**
     * 添加货品类型
     *
     * @author loki
     * @date 2020/08/25 15:38
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(GoodsTypeAddReq req) {
        if (req.getParentId() == null) {
            Integer count = this.stockGoodsTypeRepository.findPidIsNull();
            if (count > 0) {
                throw new BizException("", "请选择父节点");
            }
        }
        //校验名称唯一性
        List<StockGoodsType> goodsTypeList = this.stockGoodsTypeRepository.findByNameAndPid(req.getName(), req.getParentId());
        if (!CollectionUtils.isEmpty(goodsTypeList)) {
            throw new BizException("", "类别名称已存在");
        }


        StockGoodsType goodsType = new StockGoodsType();
        goodsType.setName(req.getName());
        goodsType.setRemark(req.getRemark());

        if (req.getParentId() != null) {
            StockGoodsType parentGoodsType = stockGoodsTypeRepository.findById(req.getParentId())
                    .orElseThrow(() -> new BizException("上级类型不存在"));
            goodsType.setPid(parentGoodsType.getId());
        }

        this.stockGoodsTypeRepository.save(goodsType);
    }


    /**
     * 修改货品类型
     *
     * @author
     * @date 2020/08/26
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(GoodsTypeUpdateReq req) {
        StockGoodsType goodsType = stockGoodsTypeRepository.findById(req.getId())
                .orElseThrow(() -> new BizException("商品类别不存在"));

        if (null != req.getParentId()) {
            List<StockGoodsType> goodsTypes = stockGoodsTypeRepository.findByNameAndPid(req.getName(), req.getParentId());
            if (!CollectionUtils.isEmpty(goodsTypes)) {
                StockGoodsType existStockGoods = goodsTypes.stream().filter(item -> !req.getId().equals(item.getId())).findAny().orElse(null);
                if (null != existStockGoods) {
                    throw new BizException("类别名称已存在");
                }
            }

            StockGoodsType parentGoodsType = stockGoodsTypeRepository.findById(req.getParentId())
                    .orElseThrow(() -> new BizException("上级类别不存在"));
            goodsType.setPid(parentGoodsType.getId());
        }

        goodsType.setName(req.getName());
        goodsType.setRemark(req.getRemark());
        goodsType.setUpdateTime(LocalDateTime.now());
        stockGoodsTypeRepository.save(goodsType);
    }

    /**
     * 删除货品类型
     *
     * @author
     * @date 2020/08/26 16:26
     */
    @Transactional(rollbackFor = Exception.class)
    public Object delete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的商品类别");
        }

        List<StockGoods> goodsList;
        List<StockGoodsType> goodsTypeList = new ArrayList<>();
        for (Long id : ids) {
            StockGoodsType goodsType = this.stockGoodsTypeRepository.findById(id).orElse(null);
            if (null == goodsType) {
                throw new BizException("", "商品类别不存在");
            }

            //校验商品类别是否被使用
            goodsList = this.stockGoodsRepository.findByType(goodsType);
            if (!CollectionUtils.isEmpty(goodsList)) {
                throw new BizException("", "商品类别【" + goodsType.getName() + "】已被使用，不能删除");
            }

            goodsTypeList.add(goodsType);
        }

        this.stockGoodsTypeRepository.deleteAll(goodsTypeList);
        return goodsTypeList;
    }
}
