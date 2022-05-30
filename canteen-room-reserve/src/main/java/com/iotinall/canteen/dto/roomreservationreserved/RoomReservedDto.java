package com.iotinall.canteen.dto.roomreservationreserved;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constant.RoomTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "包间预定列表")
public class RoomReservedDto {

    @ApiModelProperty(value = "包间Id")
    private Long id;

    @ApiModelProperty(value = "包间图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String roomPicture;

    @ApiModelProperty(value = "包间名称")
    private String roomName;

    @ApiModelProperty(value = "包间人数")
    private Integer numberOfPeople;

    @ApiModelProperty(value = "包间状态")
    private RoomTypeEnum roomTypeEnum;

    @ApiModelProperty(value = "可否预定 0：false,1：true")
    private boolean freeTime;

    @ApiModelProperty(value = "可否预定")
    private String freeTimeName;

}
