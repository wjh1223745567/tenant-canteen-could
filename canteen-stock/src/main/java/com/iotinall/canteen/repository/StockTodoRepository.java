package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockBill;
import com.iotinall.canteen.entity.StockFlwTask;
import com.iotinall.canteen.entity.StockTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 待办列表持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockTodoRepository extends JpaRepository<StockTodo, Long>, JpaSpecificationExecutor<StockTodo> {
    /**
     * 根据单据号查询待办
     */
    List<StockTodo> findByStockBill(StockBill bill);

    /**
     * 查询待办列表
     */
    List<StockTodo> findByStockBillAndTaskAndStatus(StockBill bill, StockFlwTask task, Integer status);

}
