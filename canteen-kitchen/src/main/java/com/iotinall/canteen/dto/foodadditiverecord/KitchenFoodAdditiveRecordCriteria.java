package com.iotinall.canteen.dto.foodadditiverecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-10 14:24:45
*/
@Data
@ApiModel(description = "查询添加剂记录条件")
public class KitchenFoodAdditiveRecordCriteria{

    @ApiModelProperty(value = "产品名称")
    private String keywords; // 产品名称

    @ApiModelProperty(value = "开始添加时间")
    private LocalDate beginRecordTime; // 开始添加时间

    @ApiModelProperty(value = "结束添加时间")
    private LocalDate endRecordTime; // 结束添加时间

    @ApiModelProperty(value = "状态")
    private Integer state;
}