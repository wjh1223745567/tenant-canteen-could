package com.iotinall.canteen.dto.roomreservationreserved;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "包间预定查询参数")
public class RoomReservedCondition {

    @ApiModelProperty(value = "预定日期")
    private LocalDate reservedTime;

    @ApiModelProperty(value = "预定餐次 1:早餐，2:午餐，4:晚餐")
    private Integer type;

    @ApiModelProperty(value = "1:包间人数小于等于10，2:包间人数大于等于10,小于等于15，3:包间人数大于15")
    private Integer numberPeople;

}

