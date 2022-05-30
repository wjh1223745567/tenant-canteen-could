package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.TimedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimedTaskRepository extends JpaRepository<TimedTask, Long>, JpaSpecificationExecutor<TimedTask> {

}
