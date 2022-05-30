package com.iotinall.canteen.dto.assessrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 考核人员查询
 */
@Data
public class AssessRecordEmpReq {

    @ApiModelProperty(value = "开始时间，年月日转换后的时间", required = true)
    @NotNull(message = "请先选取时间")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束时间，", required = true)
    @NotNull(message = "请先选取时间")
    private LocalDate endDate;

    @ApiModelProperty(value = "类型，0月度， 1季度， 2年度")
    @NotNull(message = "季度类型不能为空")
    private Integer typ;

    @ApiModelProperty(value = "true：已考核人员， false:未考核人员, null:全部人员")
    @NotNull(message = "请选择已考核或未考核人员类型")
    private Boolean assessed;

}
