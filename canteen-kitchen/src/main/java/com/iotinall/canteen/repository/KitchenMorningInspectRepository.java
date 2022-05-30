package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.KitchenMorningInspectRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface KitchenMorningInspectRepository extends JpaRepositoryEnhance<KitchenMorningInspectRecord, Long>, JpaSpecificationExecutor<KitchenMorningInspectRecord> {

    List<KitchenMorningInspectRecord> findAllByRecordTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "select p from KitchenMorningInspectRecord p " +
            "where p.empId = :empId and p.recordTime >= :beginTime and p.recordTime <= :endTime")
    List<KitchenMorningInspectRecord> queryByEmpIdAndRecordTime(@Param("empId") Long empId, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 获取每日晨检数量
     */
    @Query(value = "select count(distinct(record.emp_id)) " +
            " from kitchen_morning_inspect_record record" +
            " where date_format(record.record_time,'%Y-%m-%d')=:date", nativeQuery = true)
    Long statMorningInspectByDate(LocalDate date);
}
