package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockSupplier;
import com.iotinall.canteen.entity.StockSupplierType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 供应商持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockSupplierRepository extends JpaRepository<StockSupplier, Long>, JpaSpecificationExecutor<StockSupplier> {

    /**
     * 根据名称查找
     */
    StockSupplier findByName(String name);

    /**
     * 根据供应商类型统计
     */
    Long countByType(StockSupplierType type);

}
