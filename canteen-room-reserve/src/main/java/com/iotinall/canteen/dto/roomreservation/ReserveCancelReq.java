package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "审核")
public class ReserveCancelReq {

    private Long reserveId;

    private Integer type;

    private String remark;

}
