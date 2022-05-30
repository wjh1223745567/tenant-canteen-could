package com.iotinall.canteen.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @description:添加理发室请求参数
 * @author: JoeLau
 * @time: 2021年06月23日 17:16:28
 */

@Data
@ApiModel(description = "添加理发室请求参数")
public class HaircutRoomAddReq {
    /**
     * 理发室名字
     */
    @ApiModelProperty(value = "理发室名称")
    @NotBlank(message = "理发室名称不能为空")
    private String name;

    /**
     * 理发室介绍
     */
    @ApiModelProperty(value = "理发室介绍")
    private String presentation;

    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人手机号码")
    @NotBlank(message = "联系人手机号码不能为空")
    private String contactPhoneNumber;

    /**
     * 理发室地址
     */
    @ApiModelProperty(value = "地址")
    @NotBlank(message = "理发室地址不能为空")
    private String address;

    /**
     * 营业开始时间
     */
    @ApiModelProperty(value = "营业开始时间")
    @NotNull(message = "营业开始时间不能为空")
    private LocalTime openingTime;

    /**
     * 营业结束时间
     */
    @ApiModelProperty(value = "营业结束时间")
    @NotNull(message = "营业结束时间不能为空")
    private LocalTime closingTime;

    /**
     * 节假日是否营业
     */
    @ApiModelProperty(value = "节假日是否营业：false—不营业 true-营业")
    @NotNull(message = "节假日是否营业必填")
    private Boolean openInHoliday;

    /**
     * 理发室经度
     */
    @ApiModelProperty(value = "理发室位置经度")
    @NotNull(message = "理发店位置不能为空")
    private BigDecimal longitude;

    /**
     * 理发室纬度
     */
    @ApiModelProperty(value = "理发室位置纬度")
    @NotNull(message = "理发店位置不能为空")
    private BigDecimal latitude;

}
