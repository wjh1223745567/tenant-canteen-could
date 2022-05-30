package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.HaircutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface HaircutOrderRepository extends JpaRepository<HaircutOrder, Long>, JpaSpecificationExecutor<HaircutOrder> {

    /**
     * 获取该理发室待服务-0与服务中-2的订单
     *
     * @param roomId
     * @return
     */
    @Query(value = "select count(id) from haircut_order b where (b.status = 0 or b.status = 2) and b.haircut_room_id =?1", nativeQuery = true)
    int getHaircuttingNumber(Long roomId);

    /**
     * 获取用户上一次的理发完成订单
     *
     * @param empId
     * @return
     */
    @Query(value = "select * from haircut_order b where b.emp_id =?1 and b.finished_time is not null order by b.finished_time desc limit 1 ", nativeQuery = true)
    HaircutOrder getRecentOrder(Long empId);

    /**
     * 获取该理发师待服务-0与服务中-2的订单
     *
     * @param masterId
     * @return
     */
    @Query(value = "select count(id) from haircut_order b where (b.status = 0 or b.status = 2) and b.haircut_master_id =?1", nativeQuery = true)
    int getWaitingOrderCount(Long masterId);

    /**
     * 获取该理发师待服务-0的订单
     *
     * @param masterId
     * @return
     */
    @Query(value = "select count(id) from haircut_order b where b.status = 0 and b.haircut_master_id =?1", nativeQuery = true)
    int getNotServedOrderCount(Long masterId);

    /**
     * 获取该理发师服务中-2的订单
     *
     * @return
     */
    @Query(value = "select * from haircut_order b where b.status = 2 and b.haircut_master_id =?1", nativeQuery = true)
    List<HaircutOrder> findAllDuringOrder(Long masterId);

    /**
     * 此顾客是否存在此状态的理发订单
     *
     * @param empId
     * @param status
     * @return
     */
    boolean existsByEmpIdAndStatus(Long empId, int status);

    /**
     * @param masterId
     * @return
     */
    @Query(value = "select * from haircut_order b  where (b.status = 0 or b.status = 2) and b.haircut_master_id =?1 limit 1 ", nativeQuery = true)
    HaircutOrder masterExistsUnfinishedOrder(Long masterId);

    /**
     * @param roomId
     * @return
     */
    @Query(value = "select * from haircut_order b where (b.status = 0 or b.status = 2) and b.haircut_room_id =?1 limit 1 ", nativeQuery = true)
    HaircutOrder roomExistsUnfinishedOrder(Long roomId);

    /**
     * 获取该顾客前的待服务-0的订单
     *
     * @param masterId
     * @return
     */
    @Query(value = "select count(id) from haircut_order b where b.status = 0 and UNIX_TIMESTAMP(b.pick_time)<UNIX_TIMESTAMP(?2) and b.haircut_master_id =?1", nativeQuery = true)
    int getEmpNotServedOrderCount(Long masterId, LocalDateTime pickTime);

    /**
     * 获取该顾客对应理发师此时服务中-2的订单
     *
     * @param masterId
     * @return
     */
    @Query(value = "select count(id) from haircut_order b where b.status = 2 and b.haircut_master_id =?1", nativeQuery = true)
    int getDuringOrderCount(Long masterId);
}
