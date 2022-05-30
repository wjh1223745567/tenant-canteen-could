package com.iotinall.canteen.dto.safetyinspectrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-10 11:10:12
*/
@Data
@ApiModel(description = "查询消防安全条件")
public class KitchenSafetyInspectRecordCriteria{

    @ApiModelProperty(value = "检查项")
    private Long itemId; // 检查项

    @ApiModelProperty(value = "责任人姓名")
    private String keywords; // 责任人姓名

    @ApiModelProperty(value = "开始检查时间")
    private LocalDate beginRecordTime; // 开始检查时间

    @ApiModelProperty(value = "结束检查时间")
    private LocalDate endRecordTime; // 结束检查时间

    @ApiModelProperty(value = "状态")
    private Integer state;
}