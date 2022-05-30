package com.iotinall.canteen.service;

import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.flwconfig.FlwConfigDTO;
import com.iotinall.canteen.dto.flwconfig.FlwConfigUpdateReq;
import com.iotinall.canteen.dto.flwconfig.FlwTaskDTO;
import com.iotinall.canteen.dto.flwconfig.FlwTaskUpdateReq;
import com.iotinall.canteen.entity.StockFlwConfig;
import com.iotinall.canteen.entity.StockFlwTask;
import com.iotinall.canteen.repository.StockFlwConfigRepository;
import com.iotinall.canteen.repository.StockFlwTaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程配置逻辑处理类
 *
 * @author loki
 * @date 2021/6/4 11:33
 **/
@Service
public class StockFlwConfigService {
    @Resource
    private StockFlwConfigRepository stockFlwConfigRepository;
    @Resource
    private StockFlwTaskRepository stockFlwTaskRepository;

    /**
     * 更新流程配置
     *
     * @author loki
     * @date 2021/6/4 12:00
     **/
    @Transactional(rollbackFor = Exception.class)
    public void update(FlwConfigUpdateReq req) {
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(req.getBillType());
        if (null == flwConfig) {
            flwConfig = new StockFlwConfig();
            flwConfig.setType(req.getBillType());
        }

        flwConfig.setUpdateTime(LocalDateTime.now());
        flwConfig.setVersion(System.currentTimeMillis());
        this.stockFlwConfigRepository.save(flwConfig);

        //排序 先apply,再audit,最后acceptance
        if (CollectionUtils.isEmpty(req.getTaskList())) {
            return;
        }

        for (FlwTaskUpdateReq task : req.getTaskList()) {
            if (task.getTaskDefine().equals(Constants.TASK_DEFINE.APPLY)) {
                task.setSort(0);
            } else if (task.getTaskDefine().equals(Constants.TASK_DEFINE.AUDIT)) {
                task.setSort(1);
            } else if (task.getTaskDefine().equals(Constants.TASK_DEFINE.ACCEPTANCE)) {
                task.setSort(2);
            }
        }

        List<StockFlwTask> taskList = new ArrayList<>();
        StockFlwTask task;
        FlwTaskUpdateReq taskReq;
        req.setTaskList(req.getTaskList().stream().sorted(Comparator.comparing(FlwTaskUpdateReq::getSort)).collect(Collectors.toList()));
        int length = req.getTaskList().size();
        int current = Constants.INIT_TASK_ID;
        Integer pre;
        int next;
        for (; current < length; current++) {
            task = new StockFlwTask();
            taskList.add(task);

            pre = current == Constants.INIT_TASK_ID ? null : current - 1;
            next = current == length - 1 ? Constants.END_TASK_ID : current + 1;
            task.setPreTaskId(pre);
            task.setTaskId(current);
            task.setNextTaskId(next);

            taskReq = req.getTaskList().get(current);
            task.setFlwConfig(flwConfig);
            task.setVersion(flwConfig.getVersion());
            task.setTaskDefine(taskReq.getTaskDefine());
            task.setHandlerId(taskReq.getHandlerId());
            task.setUpdateTime(LocalDateTime.now());
            task.setCreateTime(LocalDateTime.now());
        }

        taskList.add(

                initEndTask(current, flwConfig));

        this.stockFlwTaskRepository.saveAll(taskList);
    }

    /**
     * 初始化任务节点
     *
     * @author loki
     * @date 2021/6/4 11:54
     **/
    private StockFlwTask initEndTask(Integer current, StockFlwConfig flwConfig) {
        return (StockFlwTask) new StockFlwTask()
                .setPreTaskId(current)
                .setTaskId(Constants.END_TASK_ID)
                .setNextTaskId(null)
                .setFlwConfig(flwConfig)
                .setVersion(flwConfig.getVersion())
                .setTaskDefine(Constants.TASK_DEFINE.END)
                .setUpdateTime(LocalDateTime.now())
                .setCreateTime(LocalDateTime.now());
    }

    /**
     * 获取首个任务
     *
     * @author loki
     * @date 2021/6/4 12:01
     **/
    public StockFlwTask getFirstTask(String billType) {
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(billType);
        if (null != flwConfig) {

            return this.stockFlwTaskRepository.findByFlwConfigIdAndTaskIdAndVersion(
                    flwConfig.getId(),
                    Constants.INIT_TASK_ID,
                    flwConfig.getVersion()
            );
        }
        return null;
    }

    /**
     * 获取下一个任务
     *
     * @author loki
     * @date 2021/6/4 12:01
     **/
    public StockFlwTask getNextTask(StockFlwTask task) {
        return this.stockFlwTaskRepository.findByFlwConfigIdAndTaskIdAndVersion(
                task.getFlwConfig().getId(),
                task.getNextTaskId(),
                task.getVersion()
        );
    }

    /**
     * 获取任务
     *
     * @author loki
     * @date 2021/6/4 18:29
     **/
    public StockFlwTask getTask(String billType, Integer taskId, Long version) {
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(billType);

        if (null != flwConfig) {
            return this.stockFlwTaskRepository.findByFlwConfigIdAndTaskIdAndVersion(
                    flwConfig.getId(),
                    taskId,
                    version
            );
        }

        return null;
    }

    /**
     * 获取结束任务
     *
     * @author loki
     * @date 2021/8/5 9:39
     **/
    public StockFlwTask getEndTask(String billType) {
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(billType);

        if (null != flwConfig) {
            return this.stockFlwTaskRepository.findByFlwConfigIdAndTaskIdAndVersion(
                    flwConfig.getId(),
                    Constants.END_TASK_ID,
                    flwConfig.getVersion()
            );
        }

        return null;
    }

    /**
     * 获取任务
     *
     * @author loki
     * @date 2021/6/4 18:29
     **/
    public StockFlwTask getNextTask(String billType, StockFlwTask task) {
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(billType);

        if (null != flwConfig) {
            return this.stockFlwTaskRepository.findByFlwConfigIdAndTaskIdAndVersion(
                    flwConfig.getId(),
                    task.getNextTaskId(),
                    task.getVersion()
            );
        }

        return null;
    }

    /**
     * 通过类型获取流程配置
     *
     * @author loki
     * @date 2021/6/9 16:42
     **/
    public FlwConfigDTO findType(String type) {
        StockFlwConfig flwConfig = this.stockFlwConfigRepository.findByType(type);
        if (null == flwConfig) {
            return null;
        }

        FlwConfigDTO flwConfigDTO = new FlwConfigDTO();
        BeanUtils.copyProperties(flwConfig, flwConfigDTO);

        //获取流程对应的任务
        List<StockFlwTask> taskList = this.stockFlwTaskRepository.findByFlwConfigIdAndVersionOrderByTaskIdAsc(flwConfig.getId(), flwConfig.getVersion());
        if (!CollectionUtils.isEmpty(taskList)) {
            List<FlwTaskDTO> taskDTOList = new ArrayList<>();
            FlwTaskDTO flwTaskDTO;
            for (StockFlwTask task : taskList) {
                if (task.getTaskDefine().equals(Constants.TASK_DEFINE.END)) {
                    continue;
                }
                flwTaskDTO = new FlwTaskDTO();
                BeanUtils.copyProperties(task, flwTaskDTO);
                flwTaskDTO.setPre(task.getPreTaskId());
                flwTaskDTO.setCurrent(task.getTaskId());
                flwTaskDTO.setNext(task.getNextTaskId());
                flwTaskDTO.setHandlerType(task.getHandlerType());
                flwTaskDTO.setHandlerId(task.getHandlerId());
                taskDTOList.add(flwTaskDTO);
            }

            flwConfigDTO.setTaskList(taskDTOList);
        }

        return flwConfigDTO;
    }
}
