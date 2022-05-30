package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceVacateRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 请假记录 Repository
 *
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
public interface AttendanceVacateRecordRepository extends JpaRepositoryEnhance<AttendanceVacateRecord, Long>, JpaSpecificationExecutor<AttendanceVacateRecord> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    /**
     * 获取请假记录
     */
    @Query(value = "select * from attendance_vacate_record r where r.emp_id = :empId and  r.begin_time<= :beginTime and r.end_time >= :endTime ", nativeQuery = true)
    List<AttendanceVacateRecord> findByBeginTimeAndEndTime(Long empId, LocalDateTime beginTime, LocalDateTime endTime);

    @Query(value = "select r from AttendanceVacateRecord r where r.empId = ?1 and r.beginTime >= ?2 and r.endTime <= ?3 ")
    List<AttendanceVacateRecord> findByTime(Long empId, LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 获取请假成功记录
     *
     * @param empId
     * @param beginTime
     * @param endTime
     * @return
     */
    @Query(value = "select r from AttendanceVacateRecord r where r.state = 1 and r.empId = ?1 and r.beginTime >= ?2 and r.endTime <= ?3 ")
    List<AttendanceVacateRecord> findBySuccessTime(Long empId, LocalDateTime beginTime, LocalDateTime endTime);
}