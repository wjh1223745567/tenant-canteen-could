package com.iotinall.canteen.job;

import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.service.ServerMonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

@Slf4j
public class ServerMonitoringJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ServerMonitoringService serverMonitoringService = SpringContextUtil.getBean(ServerMonitoringService.class);
        serverMonitoringService.executeServerMonitoring();
    }
}
