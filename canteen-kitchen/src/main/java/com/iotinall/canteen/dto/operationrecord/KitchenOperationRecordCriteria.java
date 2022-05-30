package com.iotinall.canteen.dto.operationrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-09 15:26:01
*/
@Data
@ApiModel(description = "查询kitchen_operation_record条件")
public class KitchenOperationRecordCriteria{

    @ApiModelProperty(value = "餐次类型")
    private Integer mealType;

    @ApiModelProperty(value = "类型", allowableValues = "wash_type,chop_type", required = true)
    private String itemType;

    @ApiModelProperty(value = "开始操作时间")
    private LocalDate beginRecordTime; // 开始操作时间

    @ApiModelProperty(value = "结束操作时间")
    private LocalDate endRecordTime; // 结束操作时间

    @ApiModelProperty(value = "检查人姓名")
    private String keywords; // keywords

    @ApiModelProperty(value = "状态")
    private Integer state;
}