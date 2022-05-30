package com.iotinall.canteen.dto.flwconfig;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程配置更新请求参数
 */
@Data
@ApiModel(value = "流程配置任务对象")
public class FlwTaskDTO implements Serializable {

    private Long id;

    /**
     * 上一步
     */
    private Integer pre;

    /**
     * 当前
     */
    private Integer current;

    /**
     * 下一任务
     */
    private Integer next;

    /**
     * 任务
     * request - 申请
     * approval-审核(存在多个)
     * handle-经办
     */
    private String taskDefine;

    /**
     * 处理人，多个以逗号拼接
     */
    private String handlerId;

    private String handlerType;

    private Long flwConfigId;
}
