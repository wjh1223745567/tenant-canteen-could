package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.entity.StockGoodsType;
import com.iotinall.canteen.entity.StockWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 商品持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockGoodsRepository extends JpaRepository<StockGoods, Long>, JpaSpecificationExecutor<StockGoods> {
    /**
     * 根据商品类别id查询数量
     */
    @Query(value = "select count(p) from StockGoods p where p.type.id in (:ids)")
    Integer countByStockGoodsTypeId(@Param(value = "ids") Set<Long> ids);

    /**
     * 根据名称查询
     */
    @Query(value = "select s.* from stock_goods  s where s.name=:name limit 1", nativeQuery = true)
    StockGoods findByName(@Param("name") String name);

    /**
     * 根据类型查询商品列表
     */
    List<StockGoods> findByType(StockGoodsType type);

    /**
     * 根据货品编号查询货品
     */
    StockGoods findByCode(String code);

    /**
     * 根据仓库统计商品数
     */
    Long countByWarehouse(StockWarehouse stockWarehouse);
}
