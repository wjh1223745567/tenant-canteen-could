package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceGroupShift;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

public interface AttendanceGroupShiftRepository extends JpaRepositoryEnhance<AttendanceGroupShift, Long>, JpaSpecificationExecutor<AttendanceGroupShift> {

    @Modifying
    int deleteByGroupId(Long groupId);
}
