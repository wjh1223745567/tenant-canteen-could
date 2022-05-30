package com.iotinall.canteen.dto.foodnote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: JoeLau
 * @time: 2021年07月07日 15:10:20
 */

@Data
@ApiModel(value = "美食笔记查询条件")
public class FoodNoteAppQueryCriteria {

    @ApiModelProperty(value = "笔记类型id")
    private Long typeId;

    @ApiModelProperty(value = "关键字", notes = "用户名、笔记标题")
    private String keywords;

    @ApiModelProperty(value = "关注",notes = "关注人的美食笔记")
    private Boolean followStatus;
}
