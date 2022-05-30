package com.iotinall.canteen.dto.holiday;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class HolidayDto {

    private LocalDate date;

    private Integer week;

    @ApiModelProperty("状态，0上班，1双休休息，2调休上班，3节假日休息")
    private Integer state;

    @ApiModelProperty("节日名称")
    private String name;

}
