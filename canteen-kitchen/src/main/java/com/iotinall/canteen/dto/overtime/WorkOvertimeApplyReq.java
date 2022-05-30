package com.iotinall.canteen.dto.overtime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 审核
 */
@Data
public class WorkOvertimeApplyReq {

    @NotNull(message = "ID不能为空")
    private Long id;

    @NotNull(message = "状态不能为空")
    @Range(min = 1, max = 2, message = "数值只能是1或2")
    @ApiModelProperty(value = "1，同意，2拒绝")
    private Integer state;

    @Length(max = 255, message = "审核意见长度不能超过255")
    private String auditOpinion;

}
