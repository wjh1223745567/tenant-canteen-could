package com.iotinall.canteen.service;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.dto.backuplibraryrecord.BackupLibraryRecordDto;
import org.springframework.data.domain.Pageable;

/**
 * 数据库备份
 */
public interface BackupLibraryService {

    /**
     * 数据库备份
     */
    void executeBackupLibrary();

    PageDTO<BackupLibraryRecordDto> page(Pageable pageable);

    /**
     * 删除过期数据
     */
    void expired();
}
