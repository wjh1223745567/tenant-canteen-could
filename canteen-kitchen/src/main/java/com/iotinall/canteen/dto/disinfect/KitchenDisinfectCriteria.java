package com.iotinall.canteen.dto.disinfect;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-06 15:32:49
*/
@Data
@ApiModel(description = "查询消毒管理条件")
public class KitchenDisinfectCriteria{

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "消毒项")
    private Long itemId; // 消毒项

    @ApiModelProperty(value="消毒项名")
    private String itemName;

    @ApiModelProperty(value = "开始消毒时间")
    private LocalDate beginDisinfectTime; // 开始消毒时间

    @ApiModelProperty(value = "结束消毒时间")
    private LocalDate endDisinfectTime; // 结束消毒时间

    @ApiModelProperty(value = "姓名")
    private String keywords;
}