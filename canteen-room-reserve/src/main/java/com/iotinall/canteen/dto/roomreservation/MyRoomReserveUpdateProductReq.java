package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "重新提交菜品")
public class MyRoomReserveUpdateProductReq {

    @ApiModelProperty(value = "菜品Id",required = true)
    @NotNull(message = "请输入菜品Id")
    private Long productId;


}
