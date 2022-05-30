package com.iotinall.canteen.dto.foodnotetype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 笔记类型查询条件
 * @author: JoeLau
 * @time: 2021年07月06日 14:25:38
 */

@Data
@ApiModel(value = "笔记类型查询条件")
public class FoodNoteTypeQueryCriteria {

    /**
     * 状态 true-启用 false-禁用
     */
    @ApiModelProperty(value = "类型状态", notes = "true-启用 false-禁用")
    private Boolean status;

    @ApiModelProperty(value = "关键字", notes = "类型名称")
    private String keywords;
}
