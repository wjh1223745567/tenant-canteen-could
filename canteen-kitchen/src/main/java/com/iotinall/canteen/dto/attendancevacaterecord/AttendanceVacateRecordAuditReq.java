package com.iotinall.canteen.dto.attendancevacaterecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 审批请假记录
 *
 * @author xinbing
 * @date 2020-07-14 16:29:49
 */
@Data
@ApiModel(description = "审批请假记录")
public class AttendanceVacateRecordAuditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion; // 审核意见

    @ApiModelProperty(value = "审核状态 0-等待审批 1-审批通过 2-审批拒绝")
    private Integer state; // 状态 0-等待审批 1-审批通过 2-审批拒绝
}