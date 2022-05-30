package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockWarehouseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 仓库类型持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockWarehouseTypeRepository extends JpaRepository<StockWarehouseType, Long>, JpaSpecificationExecutor<StockWarehouseType> {
    /**
     * 根据名称查找
     */
    StockWarehouseType findByName(String name);
}
