package com.iotinall.canteen.repository;

import com.iotinall.canteen.constants.TakeoutStatus;
import com.iotinall.canteen.entity.MessTakeoutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单
 *
 * @author loki
 * @date 2021/04/29 14:46
 */
public interface MessTakeoutOrderRepository extends JpaRepository<MessTakeoutOrder, Long>, JpaSpecificationExecutor<MessTakeoutOrder> {

    List<MessTakeoutOrder> findByEmployeeIdAndSourcingStatusIn(Long employeeId, List<TakeoutStatus> statusList);

    @Modifying
    @Query(value = "update MessTakeoutOrder p set p.sourcingStatus = :status where p.id = :id and p.sourcingStatus in(0,1)")
    int pickup(@Param(value = "id") Long id, @Param(value = "status") TakeoutStatus status);

    /**
     * 获取本周的订单
     *
     * @author loki
     * @date 2021/04/15 16:07
     */
    @Query(value = "select o.* " +
            " from mess_takeout_order o " +
            " where o.create_time between :begin and :end " +
            " and o.sourcing_status in (:statusList)", nativeQuery = true)
    List<MessTakeoutOrder> findTakeoutOrderCurrentWeek(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end, @Param("statusList")List<Integer> status);


    /**
     * 查询当天最大序列号
     * @return
     */
    @Query( value = "select ifnull(max(o.serial_number + 1), 0) from mess_takeout_order o where o.create_time >= CURDATE()", nativeQuery = true)
    Integer findTodaySerialNumber();

    MessTakeoutOrder findByOrderNumber(String orderNumber);

    /**
     * 取消订单
     * @param id
     * @return
     */
    @Modifying
    @Query("update MessTakeoutOrder o set o.sourcingStatus = 3 where o.sourcingStatus = 1 and o.id = ?1 ")
    Integer cancelOrder(Long id);

}
