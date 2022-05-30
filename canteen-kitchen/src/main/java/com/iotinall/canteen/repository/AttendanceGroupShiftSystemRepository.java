package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.AttendanceGroupShiftSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceGroupShiftSystemRepository extends JpaRepository<AttendanceGroupShiftSystem, Long>, JpaSpecificationExecutor<AttendanceGroupShiftSystem> {

    @Modifying
    int deleteByGroupId(Long groupId);

    @Query("select o from AttendanceGroupShiftSystem o where o.groupId = ?1 and o.empId = ?2")
    AttendanceGroupShiftSystem findByGroupAndEmp(Long groupId, Long empId);
}
