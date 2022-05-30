package com.iotinall.canteen.protocol.statistic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
 * @author joelau
 * @date 2021/06/03 14：38
 */
@Data
@ApiModel(description = "返回统计结果")
public class ActivitySurveyStatisticDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期", required = true)
    private LocalDate startDate;

    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截至日期", required = true)
    private LocalDate endDate;

    @ApiModelProperty(value = "发布范围人数")
    private Integer totalNumber;

    @ApiModelProperty(value = "已提交人数")
    private Integer submittedNumber;

}
