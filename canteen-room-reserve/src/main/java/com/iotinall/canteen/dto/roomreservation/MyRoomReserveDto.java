package com.iotinall.canteen.dto.roomreservation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "我的预定---申请人")
public class MyRoomReserveDto {

    private Long id;

    @ApiModelProperty(value = "包间图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String roomPicture;

    @ApiModelProperty(value = "包间名称")
    private String roomName;

    @ApiModelProperty(value = "预定时间")
    private String reserveTime;

    @ApiModelProperty(value = "预定餐次")
    private String reserveMeal;

    @ApiModelProperty(value = "预定状态")
    private String reserveType;

    @ApiModelProperty(value = "用餐人数")
    private Integer numberPeople;

    @ApiModelProperty(value = "申请人姓名")
    private String empName;

    @ApiModelProperty(value = "申请人Id")
    private Long empId;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;
}
