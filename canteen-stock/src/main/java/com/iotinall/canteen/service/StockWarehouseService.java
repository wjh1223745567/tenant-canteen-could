package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.warehouse.*;
import com.iotinall.canteen.entity.StockWarehouse;
import com.iotinall.canteen.entity.StockWarehouseManager;
import com.iotinall.canteen.entity.StockWarehouseType;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.StockGoodsRepository;
import com.iotinall.canteen.repository.StockWarehouseManagerRepository;
import com.iotinall.canteen.repository.StockWarehouseRepository;
import com.iotinall.canteen.repository.StockWarehouseTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 仓库逻辑处理类
 *
 * @author loki
 * @date 2021/06/03 10:56
 */
@Slf4j
@Service
public class StockWarehouseService {
    @Resource
    private StockWarehouseRepository stockWarehouseRepository;
    @Resource
    private StockWarehouseTypeRepository stockWarehouseTypeRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private StockWarehouseManagerRepository stockWarehouseManagerRepository;
    @Resource
    private StockGoodsRepository stockGoodsRepository;

    private static final int TYPE_WAREHOUSE = 0;
    private static final int TYPE_WAREHOUSE_LOCATION = 1;
    private static final String WAREHOUSE_SPLIT = "/";

    /**
     * 获取仓库所有子位置ID
     *
     * @author loki
     * @date 2020/09/24 15:20
     */
    public Set<Long> getWarehouseChildIds(Long warehouseId) {
        if (null == warehouseId) {
            return Collections.EMPTY_SET;
        }
        StockWarehouse warehouse = this.stockWarehouseRepository.findById(warehouseId).orElseThrow(
                () -> new BizException("仓库不存在")
        );

        return getWarehouseChildIds(warehouse);
    }

    /**
     * 获取所有仓库子节点ID列表
     *
     * @author loki
     * @date 2020/09/24 15:07
     */
    public Set<Long> getWarehouseChildIds(StockWarehouse warehouse) {
        Set<Long> ids = new HashSet<>();
        ids.add(warehouse.getId());

        if (!CollectionUtils.isEmpty(warehouse.getChildren())) {
            for (StockWarehouse w : warehouse.getChildren()) {
                ids.addAll(getWarehouseChildIds(w));
            }
        }

        return ids;
    }

    /**
     * 创建仓库
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    public void create(StockWarehouseAddReq req) {
        //校验名称是否重复
        StockWarehouse warehouse = stockWarehouseRepository.findByName(req.getName());
        if (null != warehouse) {
            throw new BizException("", "仓库名称已存在");
        }

        warehouse = new StockWarehouse();
        BeanUtils.copyProperties(req, warehouse);
        warehouse.setPid(req.getParentId());
        warehouse.setType(TYPE_WAREHOUSE);
        warehouse.setFullName(req.getName());
        warehouse.setRemark(req.getRemark());

        //仓库类型
        if (null != req.getWarehouseTypeId()) {
            StockWarehouseType warehouseType = this.stockWarehouseTypeRepository.findById(req.getWarehouseTypeId()).orElseThrow(
                    () -> new BizException("", "仓库类型不存在")
            );

            warehouse.setWarehouseType(warehouseType);
        } else {
            warehouse.setWarehouseType(null);
        }

        this.stockWarehouseRepository.save(warehouse);

        //管理员
        if (StringUtils.isNotBlank(req.getManagerIds())) {
            this.stockWarehouseManagerRepository.saveAll(getWarehouseManagerList(req.getManagerIds(), warehouse.getId()));
        }
    }

    /**
     * 编辑仓库
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    public void update(StockWarehouseEditReq req) {
        StockWarehouse warehouse = this.stockWarehouseRepository.findById(req.getId()).orElse(null);
        if (null == warehouse) {
            throw new BizException("", "仓库不存在");
        }

        StockWarehouse existWarehouse = stockWarehouseRepository.findByName(req.getName());
        if (null != existWarehouse && !existWarehouse.getId().equals(req.getId())) {
            throw new BizException("", "仓库名称已存在");
        }

        BeanUtils.copyProperties(req, warehouse);

        //仓库类型
        if (null != req.getWarehouseTypeId()) {
            StockWarehouseType warehouseType = stockWarehouseTypeRepository.findById(req.getWarehouseTypeId()).orElseThrow(
                    () -> new BizException("", "仓库类型不存在")
            );
            warehouse.setWarehouseType(warehouseType);
        } else {
            warehouse.setWarehouseType(null);
        }
        warehouse.setFullName(req.getName());
        warehouse.setRemark(req.getRemark());
        this.stockWarehouseRepository.save(warehouse);

        //删除管理员
        this.stockWarehouseManagerRepository.deleteAll(warehouse.getManagers());

        //管理员
        if (StringUtils.isNotBlank(req.getManagerIds())) {
            this.stockWarehouseManagerRepository.saveAll(getWarehouseManagerList(req.getManagerIds(), warehouse.getId()));
        }

        //刷新子节点下面的位置全称
        new Thread(() -> this.batchRefreshChildFullName(warehouse.getChildren(), warehouse.getName())).start();
    }

    /**
     * 获取管理员列表
     *
     * @author loki
     * @date 2021/06/03 16:43
     */
    private List<StockWarehouseManager> getWarehouseManagerList(String managerIds, Long warehouseId) {
        String[] ids = managerIds.split(",");
        List<StockWarehouseManager> managerList = new ArrayList<>();
        for (String managerId : ids) {
            FeignEmployeeDTO result = feignEmployeeService.findById(Long.valueOf(managerId));
            if (null != result) {
                managerList.add(new StockWarehouseManager()
                        .setManagerId(result.getId())
                        .setManagerName(result.getName())
                        .setWarehouseId(warehouseId)
                );
            }
        }

        return managerList;
    }

    /**
     * 创建仓库
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    public void create(StockWarehouseLocationAddReq req) {
        //校验名称是否重复
        StockWarehouse warehouse = stockWarehouseRepository.findByName(req.getName());
        if (null != warehouse) {
            throw new BizException("", "仓库名称已存在");
        }

        //校验父节点是否存在
        warehouse = new StockWarehouse();
        warehouse.setName(req.getName());
        warehouse.setType(TYPE_WAREHOUSE_LOCATION);
        warehouse.setImgUrl(req.getImgUrl());
        warehouse.setPid(req.getParentId());

        String parentName = this.getParentFullName(req.getParentId());
        warehouse.setFullName(StringUtils.isBlank(parentName) ? req.getName() : parentName + WAREHOUSE_SPLIT + req.getName());

        this.stockWarehouseRepository.save(warehouse);
    }

    /**
     * 编辑仓库
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    public void update(StockWarehouseLocationEditReq req) {
        StockWarehouse warehouse = this.stockWarehouseRepository.findById(req.getId()).orElse(null);
        if (null == warehouse) {
            throw new BizException("", "位置不存在");
        }

        //校验名称是否重复
        StockWarehouse sameNameWarehouse = stockWarehouseRepository.findByName(req.getName());
        if (null != sameNameWarehouse && !req.getId().equals(sameNameWarehouse.getId())) {
            throw new BizException("", "仓库名称已存在");
        }

        //校验父节点是否存在
        warehouse.setName(req.getName());
        warehouse.setPid(req.getParentId());
        warehouse.setImgUrl(req.getImgUrl());

        String parentName = this.getParentFullName(req.getParentId());
        warehouse.setFullName(StringUtils.isBlank(parentName) ? req.getName() : parentName + WAREHOUSE_SPLIT + req.getName());
        this.stockWarehouseRepository.save(warehouse);

        //刷新子节点下面的位置
        new Thread(() -> this.batchRefreshChildFullName(warehouse.getChildren(), warehouse.getFullName())).start();
    }

    /**
     * 获取全称
     *
     * @author loki
     * @date 2020/08/28 20:46
     */
    private String getParentFullName(Long parentId) {
        if (null != parentId && parentId != -1) {
            StockWarehouse parentWarehouse = stockWarehouseRepository.findById(parentId).orElse(null);
            if (null == parentWarehouse) {
                throw new BizException("", "父节点不存在");
            }
            return parentWarehouse.getFullName();
        }
        return "";
    }

    /**
     * 批量刷新子节点全称
     *
     * @author loki
     * @date 2020/08/28 20:35
     */
    private void batchRefreshChildFullName(Set<StockWarehouse> warehouseList, String parentName) {
        if (CollectionUtils.isEmpty(warehouseList)) {
            return;
        }

        for (StockWarehouse warehouse : warehouseList) {
            warehouse.setFullName(parentName + WAREHOUSE_SPLIT + warehouse.getName());
            this.stockWarehouseRepository.save(warehouse);

            this.batchRefreshChildFullName(warehouse.getChildren(), warehouse.getFullName());
        }
    }

    /**
     * 删除仓库
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    public void delete(Long id) {
        StockWarehouse warehouse = this.stockWarehouseRepository.findById(id).orElseThrow(
                () -> new BizException("", "仓库不存在")
        );

        if (!CollectionUtils.isEmpty(warehouse.getChildren())) {
            throw new BizException("", "存在子节点，删除失败");
        }

        //判断仓库是否已经被使用
        Long count = stockGoodsRepository.countByWarehouse(warehouse);
        if (null != count && count > 0) {
            throw new BizException("仓库已被使用，删除失败");
        }

        this.stockWarehouseRepository.delete(warehouse);
    }

    /**
     * 仓库树
     *
     * @author loki
     * @date 2020/08/28 16:17
     */
    public Object tree() {
        List<StockWarehouse> result = this.stockWarehouseRepository.findTree();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return result.stream().map(this::convertToTree).collect(Collectors.toList());
    }

    /**
     * 将LIST转成树
     *
     * @return
     */
    private StockWarehouseDTO convertToTree(StockWarehouse warehouse) {
        StockWarehouseDTO treeDTO = new StockWarehouseDTO()
                .setId(warehouse.getId())
                .setName(warehouse.getName())
                .setParentId(warehouse.getPid())
                .setAddress(warehouse.getAddress())
                .setImgUrl(warehouse.getImgUrl())
                .setFullName(warehouse.getFullName())
                .setType(warehouse.getType())
                .setRemark(warehouse.getRemark());

        //管理员
        if (!CollectionUtils.isEmpty(warehouse.getManagers())) {
            List<StockWarehouseManagerDTO> managerDTOList = new ArrayList<>();
            StockWarehouseManagerDTO managerDTO;
            for (StockWarehouseManager manager : warehouse.getManagers()) {
                managerDTO = new StockWarehouseManagerDTO();
                managerDTO.setId(manager.getManagerId());
                managerDTO.setName(manager.getManagerName());
                managerDTOList.add(managerDTO);
            }

            treeDTO.setManagers(managerDTOList);
        }

        //仓库类型
        if (null != warehouse.getWarehouseType()) {
            StockWarehouseTypeDTO warehouseTypeDTO = new StockWarehouseTypeDTO();
            warehouseTypeDTO.setId(warehouse.getWarehouseType().getId());
            warehouseTypeDTO.setName(warehouse.getWarehouseType().getName());
            treeDTO.setWarehouseType(warehouseTypeDTO);
        }

        List<StockWarehouseDTO> children = warehouse.getChildren().stream().map(this::convertToTree).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(children)) {
            treeDTO.setChildren(children);
        }
        return treeDTO;
    }

    /**
     * 获取仓库所有子位置ID
     *
     * @author loki
     * @date 2020/09/24 15:20
     */
    public Set<Long> getWarehouseAllChildIds(Long warehouseId) {
        if (null != warehouseId) {
            StockWarehouse warehouse = this.stockWarehouseRepository.findById(warehouseId).orElse(null);
            if (null == warehouse) {
                return Collections.EMPTY_SET;
            }

            Set<Long> warehouseIdList = new HashSet<>();
            warehouseIdList.add(warehouseId);
            if (!CollectionUtils.isEmpty(warehouse.getChildren())) {
                this.getWarehouseChildIds(warehouse.getChildren(), warehouseIdList);
            }
            return warehouseIdList;
        }
        return null;
    }

    /**
     * 获取所有仓库子节点ID列表
     *
     * @author loki
     * @date 2020/09/24 15:07
     */
    public void getWarehouseChildIds(Set<StockWarehouse> warehouseSet, Set<Long> childIds) {
        if (CollectionUtils.isEmpty(warehouseSet)) {
            return;
        }

        for (StockWarehouse warehouse : warehouseSet) {
            childIds.add(warehouse.getId());

            this.getWarehouseChildIds(warehouse.getChildren(), childIds);
        }
    }
}
