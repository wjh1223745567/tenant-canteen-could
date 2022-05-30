package com.iotinall.canteen.dto.roomreservationreserved;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "一周内订房信息")
public class RoomDetailSevenDaysDto {

    @ApiModelProperty(value = "周几")
    private String dayOfWeek;

    @ApiModelProperty(value = "几月几号")
    private String localDate;

    @ApiModelProperty(value = "包间时间段")
    private List<RoomDetailTimeDto> timeDtos;

}
