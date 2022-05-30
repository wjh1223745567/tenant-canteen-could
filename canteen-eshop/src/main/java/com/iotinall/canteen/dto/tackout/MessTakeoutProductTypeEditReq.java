package com.iotinall.canteen.dto.tackout;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "编辑商品类型请求参数")
public class MessTakeoutProductTypeEditReq {
    @ApiModelProperty(value = "类别Id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "类别名称")
    @NotBlank(message = "类型名称不能为空")
    @Length(max = 128)
    private String name;

    @ApiModelProperty(value = "上级类别")
    private Long parentId;

    @ApiModelProperty(value = "类别描述")
    private String remark;
}
