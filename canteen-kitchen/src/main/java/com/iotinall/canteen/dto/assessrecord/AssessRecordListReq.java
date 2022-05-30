package com.iotinall.canteen.dto.assessrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@ApiModel(value = "考核记录查询")
@Data
public class AssessRecordListReq {
    @ApiModelProperty(value = "考核类型，0-月，1-季，2-年", allowableValues = "0,1,2")
    private Integer typ;

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "员工名称")
    private String name;
}
