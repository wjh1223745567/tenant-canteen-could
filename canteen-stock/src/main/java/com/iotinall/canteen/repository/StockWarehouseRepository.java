package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockWarehouse;
import com.iotinall.canteen.entity.StockWarehouseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 仓库持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockWarehouseRepository extends JpaRepository<StockWarehouse, Long>, JpaSpecificationExecutor<StockWarehouse> {
    /**
     * 根据名称查询
     */
    StockWarehouse findByName(String name);

    /**
     * 仓库树
     */
    @Query(value = "from StockWarehouse p where p.pid is null order by p.id asc,p.createTime desc")
    List<StockWarehouse> findTree();

    /**
     * 根据仓库类型统计
     */
    Long countByWarehouseType(StockWarehouseType warehouseType);
}
