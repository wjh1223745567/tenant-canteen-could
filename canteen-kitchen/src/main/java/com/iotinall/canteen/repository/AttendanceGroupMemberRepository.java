package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.AttendanceGroupMember;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceGroupMemberRepository extends JpaRepositoryEnhance<AttendanceGroupMember, Long>, JpaSpecificationExecutor<AttendanceGroupMember> {

    @Query(value = "from AttendanceGroupMember gm where gm.groupId = :groupId")
    List<AttendanceGroupMember> findByGroupId(@Param("groupId") Long groupId);

    int deleteByGroupId(Long groupId);

    /**
     * 获取后厨人员所在的班次
     */
    AttendanceGroupMember findByEmpId(Long empId);
}
