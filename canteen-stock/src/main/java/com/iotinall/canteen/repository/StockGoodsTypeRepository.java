package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockGoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品类型持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockGoodsTypeRepository extends JpaRepository<StockGoodsType, Long>, JpaSpecificationExecutor<StockGoodsType> {
    /**
     * 商品类型树
     */
    @Query(value = "from StockGoodsType p where p.pid is null order by p.id asc")
    List<StockGoodsType> findTree();

    @Query(value = "select count(p) from StockGoodsType p where p.pid is null ")
    Integer findPidIsNull();

    /**
     * 查询相同父级下面是否存在重名类型
     *
     * @author loki
     * @date 2021/06/03 10:41
     */
    List<StockGoodsType> findByNameAndPid(String name, Long pid);

    /**
     * 根据名称查询商品类别
     */
    StockGoodsType findByName(String name);

    /**
     * 根据父节点获取商品类别
     */
    List<StockGoodsType> findByPid(Long pid);
}
