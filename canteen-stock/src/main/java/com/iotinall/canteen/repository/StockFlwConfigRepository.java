package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockFlwConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 流程配置持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockFlwConfigRepository extends JpaRepository<StockFlwConfig, Long>, JpaSpecificationExecutor<StockFlwConfig> {
    /**
     * 根据流程类型获取流程
     */
    StockFlwConfig findByType(String type);
}
