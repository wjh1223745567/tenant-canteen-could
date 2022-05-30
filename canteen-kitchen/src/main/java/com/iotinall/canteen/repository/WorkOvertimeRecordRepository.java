package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.AttendanceGroup;
import com.iotinall.canteen.entity.WorkOvertimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkOvertimeRecordRepository extends JpaRepository<WorkOvertimeRecord, Long>, JpaSpecificationExecutor<WorkOvertimeRecord> {

    /**
     * 获取上次加班卡
     * @return
     */
    @Query("select o from WorkOvertimeRecord o where o.empId = ?1 and o.group = ?2 and o.startTime <> null and o.endTime is null")
    WorkOvertimeRecord findLastRecord(Long empId, AttendanceGroup group);

    /**
     * 查询当日加班记录
     * @param employee
     * @param group
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("select o from WorkOvertimeRecord o where o.empId = ?1 and o.group = ?2 and o.createTime >= ?3 and o.createTime <= ?4")
    List<WorkOvertimeRecord> findDayRecord(Long empId, AttendanceGroup group, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取时间范围内加班时间
     * @param employee
     * @param startTime
     * @param endTime
     * @return
     */
    @Query("select o from WorkOvertimeRecord o where o.empId = ?1 and o.startTime >= ?2 and o.endTime <= ?3 ")
    List<WorkOvertimeRecord> findAllTime(Long empId, LocalDateTime startTime, LocalDateTime endTime);

    @Query("select o from WorkOvertimeRecord o where o.applyId = ?1")
    List<WorkOvertimeRecord> findByApplyId(Long id);
}
