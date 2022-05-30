package com.iotinall.canteen.dto.foodnote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 美食笔记查询条件
 * @author: JoeLau
 * @time: 2021年07月06日 15:10:54
 */

@Data
@ApiModel(value = "美食笔记查询条件")
public class FoodNoteQueryCriteria {

    @ApiModelProperty(value = "笔记类型id")
    private Long typeId;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "关键字", notes = "用户名、笔记标题")
    private String keywords;

}
