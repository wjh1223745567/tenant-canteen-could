package com.iotinall.canteen.dto.attendancesrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 考核考勤计算
 */
@Data
@Accessors(chain = true)
public class AttendanceRecordReckonDto {

    private Long empId;

    @ApiModelProperty(value = "考勤人")
    private String empName;

    @ApiModelProperty(value = "考勤人角色")
    private String empRole;

    @ApiModelProperty(value = "工作总天数")
    private Integer workDays;

    @ApiModelProperty(value = "出勤率")
    private Double attendance;

    @ApiModelProperty(value = "请假（小时）")
    private Long askForLeave;

    @ApiModelProperty(value = "加班（小时）")
    private Long overtime;

    @ApiModelProperty(value = "迟到次数")
    private Integer beLate;

    @ApiModelProperty(value = "严重迟到")
    private Integer beLateSer;

    @ApiModelProperty(value = "早退")
    private Integer leaveEarly;

    @ApiModelProperty(value = "旷工次数")
    private Integer absenteeism;
}
