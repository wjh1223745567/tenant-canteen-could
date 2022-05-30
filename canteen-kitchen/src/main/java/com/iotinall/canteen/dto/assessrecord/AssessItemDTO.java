package com.iotinall.canteen.dto.assessrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "考核项目")
public class AssessItemDTO {
    @ApiModelProperty(value = "考核类型")
    private String assessType;
    @ApiModelProperty(value = "考核类型名称")
    private String assessTypeName;
    @ApiModelProperty(value = "合格次数")
    private Integer qualifiedCount;
    @ApiModelProperty(value = "总数")
    private Integer totalCount;
    @ApiModelProperty(value = "比率，两位小数")
    private BigDecimal rate;
}
