package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:更改理发订单的状态
 * @author: JoeLau
 * @time: 2021年06月25日 15:49:48
 */

@Data
@ApiModel(description = "更改理发订单状态请求参数")
public class HaircutOrderEditReq {

    @ApiModelProperty(value = "理发订单的ID")
    @NotNull(message = "理发订单为空")
    private Long id;

    /**
     * 订单状态  0-待服务  1-已取消  2-服务中  3-已完成  4-已过号
     */
    @ApiModelProperty(value = "0-待服务  1-已取消  2-服务中  3-已完成  4-已过号")
    @NotNull(message = "理发订单的处理操作为空")
    private Integer status;
}
