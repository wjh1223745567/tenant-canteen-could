package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.StockFlwTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 流程任务持久化类
 *
 * @author loki
 * @date 2021/06/03 11:58
 */
public interface StockFlwTaskRepository extends JpaRepository<StockFlwTask, Long>, JpaSpecificationExecutor<StockFlwTask> {
    /**
     * 获取流程任务
     *
     * @param flwConfigId 流程配置
     * @param version     任务版本
     * @return 任务列表
     */
    List<StockFlwTask> findByFlwConfigIdAndVersionOrderByTaskIdAsc(Long flwConfigId, Long version);

    /**
     * 获取流程任务
     *
     * @param flwConfigId 流程配置
     * @param taskId      任务id
     * @param version     任务版本
     * @return 任务
     */
    StockFlwTask findByFlwConfigIdAndTaskIdAndVersion(Long flwConfigId, Integer taskId, Long version);

    /**
     * 获取指定环节的任务列表
     */
    List<StockFlwTask> findByFlwConfigIdAndVersionAndTaskDefine(Long flwConfigId, Long version, String taskDefine);
}
