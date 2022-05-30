package com.iotinall.canteen.protocol.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author joelau
 * @date 2021/06/03 15:02
 */
@Data
@ApiModel(description = "返回选项统计")
public class ActivitySubjectOptionStatisticDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "选项名",required = true)
    private String name;

    @ApiModelProperty(value = "选中人数", required = true)
    private Integer selectedNum;

    @ApiModelProperty(value = "占比",required = true)
    private String percent;
}
