package com.iotinall.canteen.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @description:理发师DTO
 * @author: JoeLau
 * @time: 2021年06月24日 14:21:24
 */

@Data
@ApiModel(description = "返回理发师")
public class HaircutMasterDTO {

    @ApiModelProperty(value = "主键", required = true)
    private Long id;
    /**
     * 理发师姓名
     */
    @ApiModelProperty(value = "理发师姓名")
    private String name;

    /**
     * 理发师联系方式
     */
    @ApiModelProperty(value = "理发师联系方式")
    private String phoneNumber;

    /**
     * 所属理发室
     */
    @ApiModelProperty(value = "所属理发室")
    private String haircutRoomName;

    /**
     * 工作年限
     */
    @ApiModelProperty(value = "工作年限")
    private Integer workingYears;

    /**
     * 参加工作日期
     */
    @ApiModelProperty(value = "参加工作日期")
    private LocalDate workingStart;
    /**
     * 理发师介绍
     */
    @ApiModelProperty(value = "理发师介绍")
    private String presentation;

    /**
     * 头像
     */
    @ApiModelProperty(value = "理发师头像")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String avatar;

    /**
     * 待理发人数
     */
    @ApiModelProperty(value = "待理发人数")
    private Integer waitingNumber;

    /**
     * 预计等待时间
     */
    @ApiModelProperty(value = "预计等待时间")
    private String waitingTime;

    /**
     * 理发室ID
     */
    @ApiModelProperty(value = "理发室的ID")
    private Long haircutRoomId;

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private Long empId;
}
