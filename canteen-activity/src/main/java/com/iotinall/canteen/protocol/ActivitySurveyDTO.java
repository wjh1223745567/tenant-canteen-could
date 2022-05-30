package com.iotinall.canteen.protocol;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author joelau
 * @date 2021/05/31 13:52
 */
@Data
@ApiModel(description = "返回投票活动简要记录")
public class ActivitySurveyDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "类型 0-投票活动 1-问卷调查")
    private Integer type;

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "置顶")
    private Boolean sticky;

    @ApiModelProperty(value = "状态")
    private Boolean state;

    @ApiModelProperty(value = "发布范围")
    private String orgIdList;

    @ApiModelProperty(value = "题目")
    private List<ActivitySubjectDTO> subjects;

    @ApiModelProperty(value = "封面")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "投票活动介绍")
    private String description;
}
