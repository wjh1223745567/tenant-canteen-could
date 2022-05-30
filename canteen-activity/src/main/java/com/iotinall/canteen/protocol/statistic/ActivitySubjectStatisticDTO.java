package com.iotinall.canteen.protocol.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author joelau
 * @date 2021/06/03 14:51
 */

@Data
@ApiModel(description = "返回投票活动统计的题目详情")
public class ActivitySubjectStatisticDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "题目类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer seq;

    @ApiModelProperty(value = "选项统计结果")
    private List<ActivitySubjectOptionStatisticDTO> options;

    @ApiModelProperty(value ="问答题答案列表")
    private List<ActivityTextAnswerStatisticDTO> textAnswers;
}
