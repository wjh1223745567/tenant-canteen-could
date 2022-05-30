package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.job.BackupLibraryExpiredJob;
import com.iotinall.canteen.job.BackupLibraryJob;
import com.iotinall.canteen.job.ServerMonitoringJob;
import com.iotinall.canteen.job.TestJob;
import lombok.Getter;
import org.quartz.Job;

import java.util.Objects;

@Getter
public enum JobTypeEnum {

    TEST_JOB(0, "测试定时任务", TestJob.class),
    BACKUP_LIBRARY(1, "数据库备份", BackupLibraryJob.class),
    SERVER_MONITORING(2, "检测服务器运行", ServerMonitoringJob.class),
    BACKUP_LIBRARY_EXPIRED(3, "删除过期数据库备份", BackupLibraryExpiredJob.class);

    JobTypeEnum(Integer code, String name, Class<? extends Job> jobClass) {
        this.code = code;
        this.name = name;
        this.jobClass = jobClass;
    }

    public static JobTypeEnum findByCode(Integer code) {
        for (JobTypeEnum value : JobTypeEnum.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        throw new BizException("", "未知定时任务类型");
    }

    private final Integer code;

    private final String name;

    private final Class<? extends Job> jobClass;

}
