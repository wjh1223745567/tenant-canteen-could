package com.iotinall.canteen.protocol;

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
public class ActivitySubjectDTO {
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "题目类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "选项")
    private List<String> optionList;

    @ApiModelProperty(value = "问答题提示")
    private String tips;


}
