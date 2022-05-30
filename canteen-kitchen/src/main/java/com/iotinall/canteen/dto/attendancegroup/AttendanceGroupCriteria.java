package com.iotinall.canteen.dto.attendancegroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
* @author xinbing
* @date 2020-07-13 20:28:37
*/
@Data
@ApiModel(description = "查询考勤组条件")
public class AttendanceGroupCriteria{

    @ApiModelProperty(value = "名称")
    private String name; // 名称
}