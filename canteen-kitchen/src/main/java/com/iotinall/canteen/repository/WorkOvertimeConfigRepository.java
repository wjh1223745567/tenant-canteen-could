package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.WorkOvertimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkOvertimeConfigRepository extends JpaRepository<WorkOvertimeConfig, Long>, JpaSpecificationExecutor<WorkOvertimeConfig> {
}
