package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockGoods;
import com.iotinall.canteen.entity.StockWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 库存预警持久化类
 *
 * @author loki
 * @date 2021/06/02 20:42
 */
public interface StockWarningRepository extends JpaRepository<StockWarning, Long>, JpaSpecificationExecutor<StockWarning> {
    /**
     * 获取商品预警信息
     */
    StockWarning findByGoods(StockGoods goods);
}
