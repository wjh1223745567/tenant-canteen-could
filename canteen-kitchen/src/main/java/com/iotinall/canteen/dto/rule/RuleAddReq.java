package com.iotinall.canteen.dto.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "规则添加")
public class RuleAddReq {
    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "类型id")
    @NotNull(message = "类型不能为空")
    private Long typeId;

    @ApiModelProperty(value = "简介")
    @NotBlank(message = "简介不能为空")
    private String intro;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "启用/禁用")
    @NotNull(message = "请选择启用/禁用")
    private Boolean enabled;
}
