package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @description:
 * @author: JoeLau
 * @time: 2021年06月24日 15:41:46
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "返回小程序理发室")
public class HaircutRoomAppDTO extends HaircutRoomDTO{

    @ApiModelProperty(value = "正在剪发人数")
    private int haircuttingNumber;

    @ApiModelProperty(value = "是否上次光顾")
    private boolean visited;

    @ApiModelProperty(value = "理发店距离")
    private BigDecimal distance;

    @ApiModelProperty(value = "理发店显示距离")
    private String stringDistance;
}
