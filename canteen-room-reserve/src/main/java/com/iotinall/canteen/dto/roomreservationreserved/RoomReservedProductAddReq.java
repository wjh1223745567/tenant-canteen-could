package com.iotinall.canteen.dto.roomreservationreserved;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "订房信息---菜品")
public class RoomReservedProductAddReq implements Serializable {

    @ApiModelProperty(value = "菜品Id")
    private Long productId;

    @ApiModelProperty(value = "菜品Id")
    private String productName;
}
