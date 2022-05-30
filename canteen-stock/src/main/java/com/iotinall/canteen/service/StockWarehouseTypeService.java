package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.warehouse.StockWarehouseTypeAddReq;
import com.iotinall.canteen.dto.warehouse.StockWarehouseTypeDTO;
import com.iotinall.canteen.dto.warehouse.StockWarehouseTypeEditReq;
import com.iotinall.canteen.entity.StockWarehouseType;
import com.iotinall.canteen.repository.StockWarehouseRepository;
import com.iotinall.canteen.repository.StockWarehouseTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 仓库类型逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 10:56
 */
@Slf4j
@Service
public class StockWarehouseTypeService {
    @Resource
    private StockWarehouseTypeRepository stockWarehouseTypeRepository;
    @Resource
    private StockWarehouseRepository stockWarehouseRepository;

    /**
     * 分页
     *
     * @author loki
     * @date 2020/08/31 17:39
     */
    public PageDTO<StockWarehouseTypeDTO> page(Pageable page) {
        Page<StockWarehouseType> result = this.stockWarehouseTypeRepository.findAll(page);

        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(Collections.EMPTY_LIST, result);
        }


        List<StockWarehouseTypeDTO> typeList = new ArrayList<>();
        result.stream().forEach(item -> {
            StockWarehouseTypeDTO type = new StockWarehouseTypeDTO().setId(item.getId()).setName(item.getName()).setRemark(item.getRemark());
            typeList.add(type);
        });

        return PageUtil.toPageDTO(typeList, result);
    }

    /**
     * 创建仓库类型
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    @Transactional(rollbackFor = Exception.class)
    public Object create(StockWarehouseTypeAddReq req) {
        //判断名称是否重复
        StockWarehouseType type = this.stockWarehouseTypeRepository.findByName(req.getName());
        if (null != type) {
            throw new BizException("", "仓库类型名称已经存在");
        }

        StockWarehouseType warehouseType = new StockWarehouseType();
        warehouseType.setName(req.getName());
        warehouseType.setRemark(req.getRemark());
        this.stockWarehouseTypeRepository.save(warehouseType);

        return warehouseType;
    }

    /**
     * 编辑仓库类型
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StockWarehouseTypeEditReq req) {
        StockWarehouseType type = this.stockWarehouseTypeRepository.findById(req.getId()).orElseThrow(
                () -> new BizException("", "仓库类型不存在")
        );

        //判断名称是否重复
        StockWarehouseType typeNameExist = this.stockWarehouseTypeRepository.findByName(req.getName());
        if (null != typeNameExist && !typeNameExist.getId().equals(req.getId())) {
            throw new BizException("", "仓库类型名称已经存在");
        }

        type.setName(req.getName());
        type.setRemark(req.getRemark());
        this.stockWarehouseTypeRepository.save(type);
    }


    /**
     * 删除仓库
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的仓库类型");
        }

        StockWarehouseType warehouseType;
        Long count;
        for (Long id : ids) {
            warehouseType = this.stockWarehouseTypeRepository.findById(id).orElseThrow(
                    () -> new BizException("仓库类型不存在")
            );

            //判断仓库类型是否被使用
            count = stockWarehouseRepository.countByWarehouseType(warehouseType);
            if (null != count && count > 0) {
                throw new BizException("", "仓库类型" + warehouseType.getName() + "已被使用");
            }

            this.stockWarehouseTypeRepository.delete(warehouseType);
        }
    }
}
