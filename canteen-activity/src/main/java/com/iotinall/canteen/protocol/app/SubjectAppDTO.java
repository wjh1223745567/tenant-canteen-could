package com.iotinall.canteen.protocol.app;

import com.iotinall.canteen.protocol.ActivitySubjectOptionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author joelau
 * @date 2021/06/01 10：11
 */
@Data
@ApiModel(description = "返回题目详情")
public class SubjectAppDTO {
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "题目类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "选项")
    private List<ActivitySubjectOptionDTO> optionList;

    @ApiModelProperty(value = "问答题提示")
    private String tips;

    @ApiModelProperty(value = "排序")
    private Integer seq;

    @ApiModelProperty(value = "判断是否为最后一题")
    private boolean isLast;

    @ApiModelProperty(value = "答题记录")
    private List<ActivitySubjectOptionDTO> results;

    @ApiModelProperty(value = "问答题答题记录")
    private String textAnswer;

}
