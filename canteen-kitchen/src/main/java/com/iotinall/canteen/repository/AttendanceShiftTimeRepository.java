package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.AttendanceShift;
import com.iotinall.canteen.entity.AttendanceShiftTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttendanceShiftTimeRepository extends JpaRepository<AttendanceShiftTime, Long>, JpaSpecificationExecutor<AttendanceShiftTime> {
}
