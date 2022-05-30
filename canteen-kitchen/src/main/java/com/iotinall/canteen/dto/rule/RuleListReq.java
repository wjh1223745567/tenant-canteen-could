package com.iotinall.canteen.dto.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class RuleListReq {
    @ApiModelProperty(value = "类型id")
    private Long typeId;
    @ApiModelProperty(value = "名称")
    private String keyword;
    @ApiModelProperty(value = "状态")
    private Boolean enabled;
}
