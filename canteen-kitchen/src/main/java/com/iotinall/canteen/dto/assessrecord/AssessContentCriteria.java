package com.iotinall.canteen.dto.assessrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel(value = "考核内容")
public class AssessContentCriteria {
    @ApiModelProperty(value = "员工id", required = true)
    @NotNull(message = "员工id")
    private Long empId;

    @ApiModelProperty(value = "开始日期", required = true)
    @NotNull(message = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期", required = true)
    @NotNull(message = "结束日期")
    private LocalDate endDate;
}
