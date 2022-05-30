package com.iotinall.canteen.dto.sample;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-06 17:09:03
*/
@Data
@ApiModel(description = "查询留样管理条件")
public class KitchenSampleCriteria {

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "餐次")
    private Integer mealType; // 餐次

    @ApiModelProperty(value = "开始检查时间")
    private LocalDate beginRecordTime; // 开始检查时间

    @ApiModelProperty(value = "结束检查时间")
    private LocalDate endRecordTime; // 结束检查时间

    @ApiModelProperty(value = "姓名")
    private String keywords; // 姓名

    @ApiModelProperty(value="菜谱名称")
    private String foodsName;
}