package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @description:理发室DTO
 * @author: JoeLau
 * @time: 2021年06月23日 18:21:04
 */

@Data
@ApiModel(description = "返回理发室")
public class HaircutRoomDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    @ApiModelProperty(value = "理发室名称", required = true)
    private String name;

    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 营业开始时间
     */
    @ApiModelProperty(value = "营业开始时间")
    private LocalTime openingTime;

    /**
     * 营业结束时间
     */
    @ApiModelProperty(value = "营业结束时间")
    private LocalTime closingTime;

    /**
     * 节假日是否营业
     */
    @ApiModelProperty(value = "节假日是否营业")
    private Boolean openInHoliday;

    /**
     * 理发室介绍
     */
    @ApiModelProperty(value = "介绍")
    private String presentation;

    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactPhoneNumber;

    /**
     * 理发室经度
     */
    @ApiModelProperty(value = "理发室经度")
    private BigDecimal longitude;

    /**
     * 理发室纬度
     */
    @ApiModelProperty(value = "理发室纬度")
    private BigDecimal latitude;

    /**
     * 理发室地图地址
     */
    @ApiModelProperty(value = "理发室地图地址")
    private String mapAddress;

}
