package com.iotinall.canteen.dto.assessrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 考核内容 季度年度
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "季度年度考核")
public class AssessRecordTableListDto {

    @ApiModelProperty(value = "人员姓名")
    private String empName;

    @ApiModelProperty(value = "人员职位")
    private String empRole;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "考核结果")
    private String result;

    @ApiModelProperty(value = "考核评语")
    private String comment;

}
