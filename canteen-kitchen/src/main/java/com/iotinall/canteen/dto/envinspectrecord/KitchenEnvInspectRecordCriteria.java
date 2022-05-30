package com.iotinall.canteen.dto.envinspectrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-10 13:48:40
*/
@Data
@ApiModel(description = "查询环境卫生条件")
public class KitchenEnvInspectRecordCriteria{

    @ApiModelProperty(value = "打扫区域")
    private Long itemId; // 打扫区域

    @ApiModelProperty(value = "责任人")
    private String keywords; // 责任人

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginRecordTime;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endRecordTime;

    @ApiModelProperty(value = "餐次类型")
    private Integer mealType; // 餐次类型

    @ApiModelProperty(value = "状态")
    private Integer state;
}