package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @description:订单查询条件
 * @author: JoeLau
 * @time: 2021年06月25日 16:25:52
 */
@Data
public class HaircutOrderQueryReq {

    /**
     * 查询的开始时间
     */
    @ApiModelProperty(value = "查询条件开始时间", notes = "格式为字符串yyyy-MM-dd")
    private String startTime;

    /**
     * 查询的结束时间
     */
    @ApiModelProperty(value = "查询条件结束时间", notes = "格式为字符串yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(value = "理发室ID")
    private Long roomId;

    @ApiModelProperty(value = "理发师ID")
    private Long masterId;

    @ApiModelProperty(value = "订单状态", notes = "0-待服务  1-已取消  2-服务中  3-已完成  4-已过号")
    private Integer status;

    @ApiModelProperty(value = "关键字", notes = "顾客姓名或理发师姓名")
    private String keywords;

    @ApiModelProperty(value = "是否为后台查询", notes = "true-后台查询，可以查所有订单  false-小程序查询，只能查到自己的订单")
    private Boolean exhaustive;

}
