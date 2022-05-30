package com.iotinall.canteen.protocol.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author joelau
 * @date 2021/06/01 09:43
 */
@Data
@ApiModel(description = "App问卷调查列表")
public class ActivitySurveyAppDTO {
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "有效日期")
    private String effectiveDate;

    @ApiModelProperty(value = "答题状态",notes = "0-进行中，1-已参与，2-已过期，3-还未到")
    private Integer surveyState;

    @ApiModelProperty(value = "封面")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "题目数量")
    private Integer subjectNumber;

    @ApiModelProperty(value = "活动介绍")
    private String description;

}
