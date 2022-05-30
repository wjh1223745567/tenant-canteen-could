package com.iotinall.canteen.protocol;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "添加投票活动请求参数")
public class ActivitySurveyAddReq {
    @ApiModelProperty(value = "活动类型：0-投票活动 1-问卷调查")
    @NotNull(message = "活动类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "投票活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String title;

    @ApiModelProperty(value = "投票活动介绍")
    private String description;

    @ApiModelProperty(value = "开始日期")
    @NotBlank(message = "开始日期不能为空")
    private String startDate;

    @ApiModelProperty(value = "截止日期")
    @NotBlank(message = "截止日期不能为空")
    private String endDate;

    @ApiModelProperty(value = "组织部门/发布范围ID,逗号分隔")
//    勿删@NotBlank(message = "组织部门Id不能为空")
    private String orgIdList;

    @ApiModelProperty(value = "是否置顶")
    @NotNull(message = "请设置是否置顶")
    private Boolean sticky;

    @ApiModelProperty(value = "是否启用（状态）")
    @NotNull(message = "请设置是否启用")
    private Boolean state;

    @ApiModelProperty(value = "活动封面")
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    private String imgUrl;

    @ApiModelProperty(value = "题目Subject")
    @NotEmpty(message = "活动内容不能为空")
    @NotNull(message = "活动内容不能为空")
    private List<SubjectReq> subjects;

    @Data
    @ApiModel(description = "活动题目subject")
    @Valid
    public static class SubjectReq {
        @ApiModelProperty(value = "问题类型 0-单选 1-多选 2-问答")
        @NotNull(message = "问题类型不能为空")
        private Integer type;
        @ApiModelProperty(value = "问题名称")
        @NotBlank(message = "问题名称不能为空")
        private String name;
        @ApiModelProperty(value = "问题提示")
        private String tips;
        @ApiModelProperty(value = "问题选项")
        private List<String> optionList;
    }
}
