package com.iotinall.canteen.dto.roomreservation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "包间管理查询参数")
public class RoomReservationDTO {

    private Long id;

    @ApiModelProperty(value = "包间名称")
    private String roomName;

    @ApiModelProperty(value = "包间图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String roomPicture;

    @ApiModelProperty(value = "容纳人数 为0时不显示该时间")
    private Integer people;

    @ApiModelProperty(value = "接待时间")
    private String time;

    @ApiModelProperty(value = "包间位置")
    private String roomAddress;

    @ApiModelProperty(value = "包间状态 0为空闲，1为预定")
    private int roomReserved;

    @ApiModelProperty(value = "包间状态名称")
    private String roomReservedName;

    /**
     * 时间
     */
    private List<RoomReservationTimeUpdateReq> timeReqs;

}
