package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 库存持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {
    /**
     * 获取库存数量前5的库存
     */
    @Query(value = "select  stock from Stock stock order by stock.amount DESC")
    Page<Stock> findTop5(Pageable page);
}
