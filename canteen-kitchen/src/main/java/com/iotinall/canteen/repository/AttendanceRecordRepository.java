package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceRecord;
import com.iotinall.canteen.entity.AttendanceShiftRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 考勤记录
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
public interface AttendanceRecordRepository extends JpaRepositoryEnhance<AttendanceRecord, Long>, JpaSpecificationExecutor<AttendanceRecord> {
    /**
     * 获取今日打卡记录
     */
    AttendanceRecord queryByEmpIdAndRecordDate(Long empId, LocalDate recordDate);

    @Query(nativeQuery = true, value = "select record.record_date,shift.* from attendance_shift_record shift " +
            "left outer join attendance_record record on(shift.record_id = record.id) " +
            "where record.emp_id = :empId and record.record_date = :recordDate " +
            "and (shift.shift_begin_time>=:beginTime or shift.shift_end_time<=:endTime)")
    List<AttendanceShiftRecord> findEmpVacateRecord(Long empId, LocalDate recordDate, LocalTime beginTime, LocalTime endTime);

    AttendanceRecord findByEmpIdAndRecordDate(Long empId, LocalDate date);


    List<AttendanceRecord> queryByRecordDate(LocalDate recordDate);

    @Override
    @EntityGraph(value = "detailsGraph")
    Page<AttendanceRecord> findAll(Specification spec, Pageable page);
}