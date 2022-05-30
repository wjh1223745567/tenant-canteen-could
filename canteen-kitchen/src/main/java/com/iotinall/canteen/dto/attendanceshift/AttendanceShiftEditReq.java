package com.iotinall.canteen.dto.attendanceshift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改attendance_shift
 * @author xinbing
 * @date 2020-07-13 20:01:38
 */
@Data
@ApiModel(description = "修改attendance_shift")
public class AttendanceShiftEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name; // 名称

    /**
     * 是否启用宽松时间
     */
    @ApiModelProperty(value = "是否启用宽松时间", required = true)
    @NotNull(message = "请选择是否启用宽松时间时间")
    private Boolean looseTime;

    /**
     * 可以晚到时间 MIN
     */
    @ApiModelProperty(value = "可以晚到多少时间")
    private Integer canBeLate;

    /**
     * 可以早退
     */
    @ApiModelProperty(value = "可以早退多少时间 （分钟）")
    private Integer canLeaveEarly;

    /**
     * 是否启用严重迟到
     */
    @ApiModelProperty(value = "是否启用严重迟到")
    @NotNull(message = "请选择是否启用严重迟到")
    private Boolean maxBeginTimeEnable;

    /**
     * 严重迟到时间
     */
    @ApiModelProperty(value = "严重迟到时间")
    private Integer maxBeginTime;

    /**
     * 是否启用旷工迟到
     */
    @ApiModelProperty(value = "是否启用旷工迟到")
    @NotNull(message = "是否启用旷工迟到")
    private Boolean maxEndTimeEnable;
    /**
     * 旷工迟到时间
     */
    @ApiModelProperty(value = "旷工迟到时间")
    private Integer maxEndTime;

    /**
     * 打卡时间段
     */
    @NotEmpty(message = "打卡时间段不能为空")
    @Valid
    private List<AttendanceShiftTimeDTO> timeDTOS;
}