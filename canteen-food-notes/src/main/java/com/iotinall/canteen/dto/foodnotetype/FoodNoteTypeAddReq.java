package com.iotinall.canteen.dto.foodnotetype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 美食笔记添加请求参数Req
 * @author: JoeLau
 * @time: 2021年07月06日 14:17:09
 */

@Data
@ApiModel(value = "美食笔记类型添加请求参数")
public class FoodNoteTypeAddReq {

    /**
     * 美食笔记类型名称
     */
    @ApiModelProperty(value = "笔记类型名称")
    @NotBlank(message = "笔记类型不能为空")
    private String name;

    /**
     * 状态 true-启用 false-禁用
     */
    @ApiModelProperty(value = "类型状态", notes = "true-启用 false-禁用")
    @NotNull(message = "类型状态不能为空")
    private Boolean status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
