package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceReportRecord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 考勤设备上报记录
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
public interface AttendanceReportRecordRepository extends JpaRepositoryEnhance<AttendanceReportRecord, Long>, JpaSpecificationExecutor<AttendanceReportRecord> {
}