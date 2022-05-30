package com.iotinall.canteen.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "编辑投票活动请求参数")
public class ActivitySurveyEditReq extends ActivitySurveyAddReq{

    @ApiModelProperty(value = "id")
    private Long id;

}
