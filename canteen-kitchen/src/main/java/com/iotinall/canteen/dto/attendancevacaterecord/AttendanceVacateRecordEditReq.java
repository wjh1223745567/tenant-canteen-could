package com.iotinall.canteen.dto.attendancevacaterecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 修改请假记录
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
@Data
@ApiModel(description = "修改请假记录")
public class AttendanceVacateRecordEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime; // 开始时间

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime; // 结束时间

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "请假原因")
    private String reason; // 请假原因

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion; // 审核意见

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime; // 审核时间

}