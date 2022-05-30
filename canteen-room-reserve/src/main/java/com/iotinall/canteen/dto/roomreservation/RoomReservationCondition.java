package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomReservationCondition {

    @ApiModelProperty(value = "餐厅名称")
    private String roomName;
}
