package com.iotinall.canteen.dto.flwconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程配置更新请求参数
 */
@Data
@ApiModel(value = "流程配置任务对象")
public class FlwTaskUpdateReq implements Serializable {

    @ApiModelProperty(value = "任务 apply - 申请 audit-审核(存在多个) acceptance-验收")
    private String taskDefine;

    @ApiModelProperty(value = "处理人，多个以逗号拼接")
    private String handlerId;

    private Integer sort;
}
