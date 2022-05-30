package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockBillOperateLog;
import com.iotinall.canteen.entity.StockFlwTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 单据操作日志
 *
 * @author loki
 * @date 2021/6/4 15:42
 **/
public interface StockBillOperateLogRepository extends JpaRepository<StockBillOperateLog, Long>, JpaSpecificationExecutor<StockBillOperateLog> {
    /**
     * 获取单据操作日志
     */
    StockBillOperateLog findByBillAndTask(StockBill bill, StockFlwTask task);

    @Query("select o from StockBillOperateLog o where o.bill.id = ?1 and o.task.taskId <> 99 order by o.createTime desc ")
    List<StockBillOperateLog> findByBillNoAll(Long billId);
}
