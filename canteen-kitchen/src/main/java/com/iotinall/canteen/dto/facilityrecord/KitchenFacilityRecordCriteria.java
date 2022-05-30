package com.iotinall.canteen.dto.facilityrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-10 11:32:53
*/
@Data
@ApiModel(description = "查询kitchen_facility_record条件")
public class KitchenFacilityRecordCriteria{

    @ApiModelProperty(value = "设施类型")
    private Long itemId; // 设施类型

    @ApiModelProperty(value = "设施名称")
    private String itemName; // 设施类型

    @ApiModelProperty(value = "餐次")
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "责任人")
    private String keywords; // 责任人

    @ApiModelProperty(value = "开始检查时间")
    private LocalDate beginRecordTime; // 开始检查时间

    @ApiModelProperty(value = "结束检查时间")
    private LocalDate endRecordTime; // 结束检查时间

    @ApiModelProperty(value = "状态")
    private Integer state;

}