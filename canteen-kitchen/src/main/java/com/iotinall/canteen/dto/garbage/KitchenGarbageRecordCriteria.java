package com.iotinall.canteen.dto.garbage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-10 11:46:09
*/
@Data
@ApiModel(description = "查询餐厨垃圾条件")
public class KitchenGarbageRecordCriteria{

    @ApiModelProperty(value = "餐次")
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "开始检查时间")
    private LocalDate beginRecordTime; // 开始检查时间

    @ApiModelProperty(value = "结束检查时间")
    private LocalDate endRecordTime; // 结束检查时间

    @ApiModelProperty(value = "责任人姓名")
    private String keywords; // 责任人姓名

    @ApiModelProperty(value = "状态")
    private Integer state;
}