package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.TimedTaskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimedTaskRecordRepository extends JpaRepository<TimedTaskRecord, Long>, JpaSpecificationExecutor<TimedTaskRecord> {

}
