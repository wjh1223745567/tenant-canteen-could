package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.BackupLibraryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BackupLibraryRecordRepository extends JpaRepository<BackupLibraryRecord, Long>, JpaSpecificationExecutor<BackupLibraryRecord> {
}
