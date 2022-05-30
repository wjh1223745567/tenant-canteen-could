package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockSupplierType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 供应商类型持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockSupplierTypeRepository extends JpaRepository<StockSupplierType, Long>, JpaSpecificationExecutor<StockSupplierType> {

    StockSupplierType findByName(String name);
}
