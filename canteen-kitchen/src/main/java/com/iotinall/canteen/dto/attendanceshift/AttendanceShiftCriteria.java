package com.iotinall.canteen.dto.attendanceshift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
* @author xinbing
* @date 2020-07-13 20:01:38
*/
@Data
@ApiModel(description = "查询attendance_shift条件")
public class AttendanceShiftCriteria{

    @ApiModelProperty(value = "名称")
    private String name; // 名称
}