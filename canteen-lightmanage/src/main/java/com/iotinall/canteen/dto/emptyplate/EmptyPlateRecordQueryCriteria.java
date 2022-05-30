package com.iotinall.canteen.dto.emptyplate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmptyPlateRecordQueryCriteria implements Serializable {
    @ApiModelProperty(value = "起始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "餐次类型" +
            "0-早餐," +
            "1-午餐," +
            "2-晚餐")
    private Integer mealType;
}

