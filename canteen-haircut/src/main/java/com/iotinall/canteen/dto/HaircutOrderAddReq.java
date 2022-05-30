package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * @description:添加理发订单请求参数
 * @author: JoeLau
 * @time: 2021年06月25日 13:56:07
 */

@Data
@ApiModel(description = "添加理发订单请求参数")
public class HaircutOrderAddReq {

    /**
     * 所属理发室
     */
    @ApiModelProperty(value = "所属理发室id")
    @NotNull(message = "理发室不能为空")
    private Long haircutRoomId;

    /**
     * 所属理发师
     */
    @ApiModelProperty(value = "所属理发师id")
    @NotNull(message = "理发师不能为空")
    private Long haircutMasterId;
}
