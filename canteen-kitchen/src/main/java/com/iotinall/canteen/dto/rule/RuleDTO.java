package com.iotinall.canteen.dto.rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "规章制度")
public class RuleDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型id")
    private Long typeId;

    @ApiModelProperty(value = "类型名")
    private String typeName;

    @ApiModelProperty(value = "类型描述")
    private String typeDesc;

    @ApiModelProperty(value = "规章简介")
    private String intro;

    @ApiModelProperty(value = "规章描述")
    private String description;

    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
