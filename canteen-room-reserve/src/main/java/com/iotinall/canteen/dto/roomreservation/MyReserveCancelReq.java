package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "取消")
public class MyReserveCancelReq {

    private Long reserveId;

    private Integer type;

}
