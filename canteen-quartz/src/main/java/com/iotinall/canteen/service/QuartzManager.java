package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author WJH
 * @date 2020/1/1610:01
 */
@Component
public class QuartzManager {

    @Resource
    private SchedulerFactoryBean factory;

    public static final String defaultGroup = "timetask";

    /**
     * jobEntity
     *
     * @param job     执行任务
     * @param dataMap 保存的参数
     * @throws SchedulerException
     */
    public void addSimpleJob(String name, String group, Integer delaySecond, Class<? extends Job> job, JobDataMap dataMap) {
        Scheduler schd = factory.getScheduler();
        //多少秒之后执行
        Date startTime = new Date(System.currentTimeMillis() + delaySecond * 1000);
        dataMap.put("jobName", name);
        dataMap.put("jobGroup", group);
        JobDetail jobDetail = JobBuilder.newJob(job)
                .withIdentity(name, group)
                .setJobData(dataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger().
                withIdentity(TriggerKey.triggerKey("trigger" + name, "trigger" + group))
                .startAt(startTime)
                .build();
        //两者组成一个计划任务注册到scheduler
        try {
            schd.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws
     * @title addDateJob
     * @description 添加到期执行的任务
     * @author haifeng.lv
     * @param: jobEntity 实体类
     * @param: job 执行类
     * @param: dataMap 数据
     * @updateTime 2019/5/14 19:30
     */
    public void addDateJob(String name, String group, LocalDateTime startTime, Class<? extends Job> job, JobDataMap dataMap) {
        Scheduler schd = factory.getScheduler();
        // 名称
        dataMap.put("jobName", name);
        // 工作组
        dataMap.put("jobGroup", group);
        // 工作详情
        JobDetail jobDetail = JobBuilder.newJob(job)
                .withIdentity(name, group)
                .setJobData(dataMap)
                .build();

        // 设置执行策略
        Trigger trigger = TriggerBuilder.newTrigger().
                withIdentity(TriggerKey.triggerKey("trigger" + name, "trigger" + group))
                // 设置开始时间
                .startAt(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        //两者组成一个计划任务注册到scheduler
        try {
            schd.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void subJob(String name, String group) {
        Scheduler schd = factory.getScheduler();
        try {
            schd.deleteJob(JobKey.jobKey(name, group));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据分组删除定时任务
     *
     * @param group
     */
    public void subJobByGroup(String group) {
        try {
            Scheduler schd = factory.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(group);
            Set<JobKey> jobkeySet = schd.getJobKeys(matcher);
            List<JobKey> jobkeyList = new ArrayList<JobKey>();
            jobkeyList.addAll(jobkeySet);
            schd.deleteJobs(jobkeyList);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addCronJob(String name, String group, String cron, Class<? extends Job> job, JobDataMap dataMap) {
        Scheduler schd = factory.getScheduler();
        //在初始化调度的时候clean一下
        dataMap.put("jobName", name);
        dataMap.put("jobGroup", group);
        JobDetail jobDetail = JobBuilder.newJob(job)
                .withIdentity(name, group)
                .setJobData(dataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                .withSchedule(
                        CronScheduleBuilder.cronSchedule(cron)
                                //失效后启动不执行错过任务
                                .withMisfireHandlingInstructionDoNothing()
                ).startNow().build();
        //两者组成一个计划任务注册到scheduler
        try {
            schd.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new BizException("", "定时任务创建失败");
        }
//        if(!schd.isShutdown()){
//            schd.start();//启动调度器
//        }
    }

}
