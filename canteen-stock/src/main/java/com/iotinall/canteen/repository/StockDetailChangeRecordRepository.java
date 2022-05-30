package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBillDetail;
import com.iotinall.canteen.entity.StockDetailChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 库存明细变动日志
 *
 * @author loki
 * @date 2021/6/4 17:43
 **/
public interface StockDetailChangeRecordRepository extends JpaRepository<StockDetailChangeRecord, Long>, JpaSpecificationExecutor<StockDetailChangeRecord> {
    /**
     * 根据单据明细获取库存明细变动记录
     */
    List<StockDetailChangeRecord> findByBillDetailOrderBySeqDesc(StockBillDetail detail);
}
