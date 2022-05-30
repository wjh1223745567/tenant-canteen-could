package com.iotinall.canteen.dto.assessrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel(value = "添加考核")
public class AssessRecordEditReq {
    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "员工id")
    @NotNull(message = "员工id不能为空")
    private Long empId;

    @ApiModelProperty(value = "考核类型：0-月度，1-季度，2-年度", allowableValues = "0,1,2", required = true)
    @NotNull(message = "考核类型不能为空")
    private Integer typ;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private LocalDate endDate;

    @ApiModelProperty(value = "考核结果")
    @NotNull(message = "考核结果不能为空")
    private Long itemId;

    @ApiModelProperty(value = "考核评语")
    private String comments;


}
