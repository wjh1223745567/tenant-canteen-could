package com.iotinall.canteen.configuration;

import com.iotinall.canteen.entity.TimedTask;
import com.iotinall.canteen.entity.TimedTaskRecord;
import com.iotinall.canteen.job.ServerMonitoringJob;
import com.iotinall.canteen.repository.TimedTaskRecordRepository;
import com.iotinall.canteen.repository.TimedTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
public class SimpleJobListener implements JobListener {

    private static TimedTaskRecordRepository timedTaskRecordRepository;

    private static TimedTaskRepository timedTaskRepository;

    private final String recordIdKey = "recordId";

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        Job job = context.getJobInstance();
        if(notIncluded(job)){
            JobDetail jobDetail = context.getJobDetail();
            String id = jobDetail.getKey().getName();

            TimedTask timedTask = new TimedTask();
            timedTask.setId(Long.valueOf(id));

            TimedTaskRecord timedTaskRecord = new TimedTaskRecord()
                    .setTimedTask(timedTask);
            timedTaskRecord = timedTaskRecordRepository.save(timedTaskRecord);
            context.put(recordIdKey, timedTaskRecord.getId());
        }
    }

    //任务调度被拒了
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("任务被拒绝：{}", context.get(recordIdKey));
    }

    //任务被调度后
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        Job job = context.getJobInstance();
        if(notIncluded(job)) {
            Long recordId = (Long) context.get(recordIdKey);
            timedTaskRecordRepository.findById(recordId).ifPresent(item -> {
                item.setEndTime(LocalDateTime.now())
                        .setSuccess(validException(jobException))
                        .setErrorLog(!validException(jobException) ? jobException.getMessage() : null);
                timedTaskRecordRepository.save(item);
                Long id = (Long) context.getJobDetail().getJobDataMap().get("id");
                timedTaskRepository.findById(id).ifPresent(timedTask -> {
                    timedTask.setLastExecuteTime(item.getEndTime())
                            .setLastSuccess(item.getSuccess());
                    timedTaskRepository.save(timedTask);
                });
            });
        }
    }

    private Boolean notIncluded(Job job){
        if(job instanceof ServerMonitoringJob){
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private Boolean validException(JobExecutionException jobExecutionException){
        if(jobExecutionException != null && StringUtils.isNotBlank(jobExecutionException.getMessage())){
            return Boolean.FALSE;
        }else{
            return Boolean.TRUE;
        }
    }

    @Resource
    public void setTimedTaskRecordRepository(TimedTaskRecordRepository timedTaskRecordRepository) {
        SimpleJobListener.timedTaskRecordRepository = timedTaskRecordRepository;
    }

    @Resource
    public void setTimedTaskRepository(TimedTaskRepository timedTaskRepository) {
        SimpleJobListener.timedTaskRepository = timedTaskRepository;
    }
}
