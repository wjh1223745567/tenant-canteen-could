package com.iotinall.canteen.dto.attendancevacaterecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 添加请假记录
 *
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
@Data
@ApiModel(description = "添加请假记录")
public class AttendanceVacateRecordAddReq {

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime; // 开始时间

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime; // 结束时间

    @ApiModelProperty(value = "请假事由")
    @NotBlank(message = "请假事由不能为空")
    private String reason; // 请假原因

}