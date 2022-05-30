package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceShift;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * attendance_shift Repository
 * @author xinbing
 * @date 2020-07-13 19:54:53
 */
public interface AttendanceShiftRepository extends JpaRepositoryEnhance<AttendanceShift, Long>, JpaSpecificationExecutor<AttendanceShift> {

    /**
     * 批量删除
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);
}