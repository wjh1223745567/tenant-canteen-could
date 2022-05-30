package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:理发订单返回DTO
 * @author: JoeLau
 * @time: 2021年06月25日 16:35:31
 */

@Data
@ApiModel(description = "返回理发订单")
public class HaircutOrderDTO {

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单id")
    private Long id;

    /**
     * 预约号码
     */
    @ApiModelProperty(value = "预约号码，即订单号")
    private String orderNumber;

    /**
     * 所属理发室
     */
    @ApiModelProperty(value = "所属理发室名称")
    private String haircutRoomName;

    /**
     * 所属理发师
     */
    @ApiModelProperty(value = "所属理发师名称")
    private String haircutMasterName;

    /**
     * 所属顾客
     */
    @ApiModelProperty(value = "所属顾客名称")
    private String empName;

    /**
     * 顾客电话号码
     */
    @ApiModelProperty(value = "顾客电话号码")
    private String empPhone;

    /**
     * 取号时间
     */
    @ApiModelProperty(value = "取号时间")
    private LocalDateTime pickTime;

    /**
     * 开始理发时间
     */
    @ApiModelProperty(value = "开始理发时间")
    private LocalDateTime startCutTime;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishedTime;

    /**
     * 取消时间
     */
    @ApiModelProperty(value = "取消时间")
    private LocalDateTime cancelTime;

    /**
     * 过号时间
     */
    @ApiModelProperty(value = "过号时间")
    private LocalDateTime passedTime;

    /**
     * 订单状态  0-待服务  1-已取消  2-服务中  3-已完成  4-已过号
     */
    @ApiModelProperty(value = "订单状态", notes = "0-待服务  1-已取消  2-服务中  3-已完成  4-已过号")
    private Integer status;

    /**
     * 前面排队人数，不包括自己
     */
    @ApiModelProperty(value = "前面排队人数", notes = "不包括该顾客")
    private Integer waitingNumber;

    /**
     * 预计等待时间
     */
    @ApiModelProperty(value = "预计等待时间")
    private String waitingTime;


//    /**
//     * 订单消费金额
//     */
//    @ApiModelProperty(value = "消费金额")
//    private Long consumption;
}
