package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockWarehouseManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 仓库管理员持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockWarehouseManagerRepository extends JpaRepository<StockWarehouseManager, Long>, JpaSpecificationExecutor<StockWarehouseManager> {

}
