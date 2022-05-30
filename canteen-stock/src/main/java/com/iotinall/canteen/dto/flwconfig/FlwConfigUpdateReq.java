package com.iotinall.canteen.dto.flwconfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 流程配置更新请求参数
 */
@Data
@ApiModel(value = "流程配置更新请求参数")
public class FlwConfigUpdateReq implements Serializable {
    @ApiModelProperty(value = "流程类型")
    @NotBlank(message = "流程类型不能为空")
    private String billType;

    @ApiModelProperty(value = "任务配置明细")
    private List<FlwTaskUpdateReq> taskList;
}
