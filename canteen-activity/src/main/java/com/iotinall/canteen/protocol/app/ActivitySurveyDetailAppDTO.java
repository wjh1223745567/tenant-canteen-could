package com.iotinall.canteen.protocol.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author joelau
 * @date 2021/06/01 11：58
 */
@Data
@ApiModel(description = "返回APP投票活动介绍")
public class ActivitySurveyDetailAppDTO {
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "活动图片")
    private String imgUrl;

    @ApiModelProperty(value = "活动介绍")
    private String description;

    @ApiModelProperty(value = "有效日期")
    private String effectiveDate;
}
