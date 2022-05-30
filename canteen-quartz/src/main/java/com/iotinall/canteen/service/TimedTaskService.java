package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.constant.JobTypeEnum;
import com.iotinall.canteen.dto.timedtask.TimedTaskDto;
import com.iotinall.canteen.dto.timedtask.TimedTaskReq;
import com.iotinall.canteen.dto.timedtask.TimedTaskView;
import com.iotinall.canteen.entity.TimedTask;
import com.iotinall.canteen.repository.TimedTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimedTaskService {

    @Resource
    private TimedTaskRepository timedTaskRepository;

    @Resource
    private QuartzManager quartzManager;

    public PageDTO<TimedTaskDto> page(Pageable pageable){
        Page<TimedTaskDto> page = this.timedTaskRepository.findAll(pageable).map(item -> {
            TimedTaskDto timedTaskDto = new TimedTaskDto()
                    .setName(item.getName())
                    .setType(item.getType())
                    .setTypeName(JobTypeEnum.findByCode(item.getType()).getName())
                    .setLastExecuteTime(item.getLastExecuteTime())
                    .setLastSuccess(item.getLastSuccess())
                    .setCorn(item.getCorn());
            timedTaskDto.setId(item.getId());
            return timedTaskDto;
        });
        return PageUtil.toPageDTO(page);
    }

    /**
     * 添加定时任务
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(TimedTaskReq req){
        TimedTask timedTask = new TimedTask()
                .setName(req.getName())
                .setType(req.getType())
                .setCorn(req.getCorn());
        timedTask.setId(req.getId())
                .setRemark(req.getRemark());
        if(req.getId() != null){
            timedTask.setId(req.getId());
            quartzManager.subJob(String.valueOf(req.getId()), QuartzManager.defaultGroup);
        }
        timedTask = this.timedTaskRepository.save(timedTask);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("id", timedTask.getId());
        quartzManager.addCronJob(String.valueOf(timedTask.getId()), QuartzManager.defaultGroup, req.getCorn(), JobTypeEnum.findByCode(req.getType()).getJobClass(), jobDataMap);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public TimedTaskView findById(Long id){
        TimedTask timedTask = this.timedTaskRepository.findById(id).orElseThrow(() -> new BizException("", "未找到数据"));
        TimedTaskView timedTaskView = new TimedTaskView()
                .setId(timedTask.getId())
                .setName(timedTask.getName())
                .setCorn(timedTask.getCorn())
                .setType(timedTask.getType())
                .setLastExecuteTime(timedTask.getLastExecuteTime())
                .setLastSuccess(timedTask.getLastSuccess())
                .setRemark(timedTask.getRemark());
        return timedTaskView;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleted(Set<Long> ids){
        if(CollectionUtils.isEmpty(ids)){
            throw new BizException("", "请选择要删除的数据");
        }
        List<TimedTask> timedTasks = ids.stream().map(item -> {
            TimedTask timedTask = new TimedTask();
            timedTask.setId(item);
            return timedTask;
        }).collect(Collectors.toList());
        this.timedTaskRepository.deleteAll(timedTasks);
        for (Long id : ids) {
            this.quartzManager.subJob(String.valueOf(id), QuartzManager.defaultGroup);
        }

    }

    /**
     * 重新启动定时任务
     */
    @PostConstruct
    public void restartQuartz(){
        List<TimedTask> timedTasks = this.timedTaskRepository.findAll();
        //清除所有任务
        this.quartzManager.subJobByGroup(QuartzManager.defaultGroup);
        //启动所有任务
        for (TimedTask timedTask : timedTasks) {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("id", timedTask.getId());
            quartzManager.addCronJob(String.valueOf(timedTask.getId()), QuartzManager.defaultGroup, timedTask.getCorn(), JobTypeEnum.findByCode(timedTask.getType()).getJobClass(), jobDataMap);
        }
    }
}
