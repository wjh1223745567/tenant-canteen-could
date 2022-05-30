package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.AttendanceGroupShiftSystemDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceGroupShiftSystemDateRepository extends JpaRepository<AttendanceGroupShiftSystemDate, Long>, JpaSpecificationExecutor<AttendanceGroupShiftSystemDate> {

    @Query(value = "select o.* from attendance_group_shift_system_date o where o.shift_system_id= ?1 and DATE_FORMAT(o.date, '%Y-%m') = ?2", nativeQuery = true)
    List<AttendanceGroupShiftSystemDate> findByShiftIdAndDate(Long shiftSystemId, String date);
}
