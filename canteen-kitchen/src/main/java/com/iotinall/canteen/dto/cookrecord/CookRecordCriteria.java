package com.iotinall.canteen.dto.cookrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-09 15:26:01
*/
@Data
@ApiModel(description = "查询Cook Record")
public class CookRecordCriteria {

    @ApiModelProperty(value = "餐次类型")
    private Integer mealType;

    @ApiModelProperty(value = "开始操作时间")
    private LocalDate beginRecordTime; // 开始操作时间

    @ApiModelProperty(value = "结束操作时间")
    private LocalDate endRecordTime; // 结束操作时间

    @ApiModelProperty(value = "检查人姓名")
    private String keywords; // keywords

    @ApiModelProperty(value = "状态")
    private Integer state;
}