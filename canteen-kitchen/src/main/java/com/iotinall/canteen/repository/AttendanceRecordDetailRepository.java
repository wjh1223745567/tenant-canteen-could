package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceRecordDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * attendance_shift Repository
 *
 * @author xinbing
 * @date 2020-07-13 19:54:53
 */
public interface AttendanceRecordDetailRepository extends JpaRepositoryEnhance<AttendanceRecordDetail, Long>, JpaSpecificationExecutor<AttendanceRecordDetail> {

}