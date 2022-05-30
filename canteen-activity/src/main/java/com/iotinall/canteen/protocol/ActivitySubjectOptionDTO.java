package com.iotinall.canteen.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author joelau
 * @date 2021/06/01 10：11
 */
@Data
@ApiModel(description = "返回题目详情")
public class ActivitySubjectOptionDTO {
    @ApiModelProperty(value = "选项id")
    private Long id;

    @ApiModelProperty(value = "选项名")
    private String name;

}
