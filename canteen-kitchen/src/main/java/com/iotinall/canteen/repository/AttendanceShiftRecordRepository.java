package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceShiftRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 考勤记录
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
public interface AttendanceShiftRecordRepository extends JpaRepositoryEnhance<AttendanceShiftRecord, Long>, JpaSpecificationExecutor<AttendanceShiftRecord> {
    AttendanceShiftRecord findByRecordIdAndShiftId(Long recordId, Long shiftId);

    /**
     * 获取打卡记录列表
     */
    List<AttendanceShiftRecord> findByPunchInStatusOrPunchOutStatus(Integer punchInStatus, Integer punchOutStatus);
}