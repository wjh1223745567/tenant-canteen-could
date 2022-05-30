package com.iotinall.canteen.job;

import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.service.BackupLibraryService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 删除过期备份数据
 */
public class BackupLibraryExpiredJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        BackupLibraryService backupLibraryService = SpringContextUtil.getBean(BackupLibraryService.class);
        backupLibraryService.expired();
    }
}
