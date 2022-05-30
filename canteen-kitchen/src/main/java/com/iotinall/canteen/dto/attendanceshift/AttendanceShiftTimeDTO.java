package com.iotinall.canteen.dto.attendanceshift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "班次时间范围")
public class AttendanceShiftTimeDTO {

    private Long id;
    /**
     * 上班时间
     */
    @ApiModelProperty(name = "上班时间")
    @NotNull(message = "上班时间部门为空")
    private LocalTime workingHours;

    /**
     * 上班开始打卡时间
     */
    @ApiModelProperty(name = "上班开始打卡时间")
    @NotNull(message = "上班开始打卡时间不能为空")
    private LocalTime workingHoursStart;

    /**
     * 上班结束打卡时间
     */
    @ApiModelProperty(name = "上班结束打卡时间")
    @NotNull(message = "上班结束打卡时间不能为空")
    private LocalTime workingHoursEnd;

    /**
     * 下班打卡时间
     */
    @ApiModelProperty(name = "下班打卡时间")
    @NotNull(message = "下班打卡时间不能为空")
    private LocalTime offWorkTime;

    /**
     * 下班开始打卡时间
     */
    @ApiModelProperty(name = "下班开始打卡时间")
    @NotNull(message = "下班开始打卡时间不能为空")
    private LocalTime offWorkTimeStart;

    /**
     * 下班结束打卡时间
     */
    @ApiModelProperty(name = "下班结束打卡时间")
    @NotNull(message = "下班结束打卡时间不能为空")
    private LocalTime offWorkTimeEnd;


}
