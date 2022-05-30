package com.iotinall.canteen.protocol.statistic;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author joelau
 * @date 2021/06/04 11:44
 */
@Data
@ApiModel(description = "返回问答题答案统计")
public class ActivityTextAnswerStatisticDTO {
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "问答题答案",required = true)
    private String textAnswer;

    @ApiModelProperty(value = "答题人姓名", required = true)
    private String empName;

}
