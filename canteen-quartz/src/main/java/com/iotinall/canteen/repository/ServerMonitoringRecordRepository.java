package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.ServerMonitoringRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServerMonitoringRecordRepository extends JpaRepository<ServerMonitoringRecord, Long>, JpaSpecificationExecutor<ServerMonitoringRecord> {
}
