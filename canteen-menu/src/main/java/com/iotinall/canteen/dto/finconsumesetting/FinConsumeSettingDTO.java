package com.iotinall.canteen.dto.finconsumesetting;

import com.iotinall.canteen.constants.MealTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 添加fin_consume_setting请求
 * @author xin-bing
 * @date 2019-10-23 17:57:09
 */
@Data
@ApiModel(description = "消费设置返回")
public class FinConsumeSettingDTO {
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "类型", required = true)
    private MealTypeEnum mealType;

    @ApiModelProperty(value = "就餐开始时间，HH:mm", required = true)
    private String beginTime;// begin_time

    @ApiModelProperty(value = "就餐截止时间，HH:mm", required = true)
    private String endTime;// end_time
}