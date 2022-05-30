package com.iotinall.canteen.dto.roomreservation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "我的预定菜品")
public class MyRoomReserveDetailMessDto {

    @ApiModelProperty(value = "菜品Id")
    private Long messId;

    @ApiModelProperty(value = "菜品图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String messPicture;

    @ApiModelProperty(value = "申请人所属部门")
    private String messName;

    private String name;

    private Long createTime;
}
