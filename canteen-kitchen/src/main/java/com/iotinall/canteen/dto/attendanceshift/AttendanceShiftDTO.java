package com.iotinall.canteen.dto.attendanceshift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author xinbing
 * @date 2020-07-13 20:01:38
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "attendance_shiftDTO")
public class AttendanceShiftDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "名称", required = true)
    private String name; // 名称

    /**
     * 是否启用宽松时间
     */
    @ApiModelProperty(value = "是否启用宽松时间")
    private Boolean looseTime;

    /**
     * 可以晚到时间 MIN
     */
    @ApiModelProperty(value = "可以晚到时间")
    private Integer canBeLate;

    /**
     * 可以早退
     */
    @ApiModelProperty(value = "可以早退")
    private Integer canLeaveEarly;

    /**
     * 是否启用严重迟到
     */
    @ApiModelProperty(value = "是否启用严重迟到")
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
    private Boolean maxEndTimeEnable;

    /**
     * 旷工迟到时间
     */
    @ApiModelProperty(value = "旷工迟到时间")
    private Integer maxEndTime;

    private List<AttendanceShiftTimeDTO> shiftTimeDTOS;

    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime; // 创建时间
}