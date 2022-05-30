package com.iotinall.canteen.protocol.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "添加答题记录请求参数")
public class AnswerRecordAddReq {

    @ApiModelProperty(value = "题目Id")
    @NotNull(message = "题目id不能为空")
    private Long subjectId;

    @ApiModelProperty(value="题目类型")
    private Integer type;

    @ApiModelProperty(value = "问答题答案")
    private String textAnswer;

    @ApiModelProperty(value = "调查活动Id")
    @NotNull(message = "调查活动不能为空")
    private Long surveyId;

    @ApiModelProperty(value = "已选选项的所有ID")
    private List<Long> optionIds;

}
