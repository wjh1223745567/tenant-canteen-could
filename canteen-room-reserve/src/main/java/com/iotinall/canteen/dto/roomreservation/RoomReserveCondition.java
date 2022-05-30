package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "包间预定查询参数")
public class RoomReserveCondition {

    private String roomName;

    private Integer toExamine;

    private LocalDate beginDate;

    private LocalDate endDate;

    private String empName;

}
