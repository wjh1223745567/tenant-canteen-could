package com.iotinall.canteen.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "投票活动列表查询条件")
public class ActivitySurveyQueryCriteria {
    @ApiModelProperty(value = "起始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "咨询标题")
    private String keywords;

    @ApiModelProperty(value = "类型")
    private Integer type;
}
