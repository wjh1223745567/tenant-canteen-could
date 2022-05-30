package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:理发师查询条件
 * @author: JoeLau
 * @time: 2021年06月24日 14:02:08
 */
@Data
public class HaircutMasterQueryCriteria {

    @ApiModelProperty(value = "关键字", notes = "理发师名")
    private String keywords;

    @ApiModelProperty(value = "理发室id")
    private Long haircutRoomId;

}
