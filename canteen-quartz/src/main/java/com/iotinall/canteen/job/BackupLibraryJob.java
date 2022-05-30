package com.iotinall.canteen.job;

import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.service.BackupLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

/**
 * 数据库备份
 */
@Slf4j
public class BackupLibraryJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("开始执行数据库备份：{}", LocalDateTime.now());
        BackupLibraryService backupLibraryService = SpringContextUtil.getBean(BackupLibraryService.class);
        backupLibraryService.executeBackupLibrary();
    }
}
