package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceGroup;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

/**
 * 考勤组 Repository
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
public interface AttendanceGroupRepository extends JpaRepositoryEnhance<AttendanceGroup, Long>, JpaSpecificationExecutor<AttendanceGroup> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    @Modifying
    int deleteByIdIn(@Param(value = "ids") Long[] ids);
}