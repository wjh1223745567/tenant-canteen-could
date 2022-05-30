package com.iotinall.canteen.dto.roomreservationreserved;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.constant.RoomTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "订房详情")
public class RoomDetailDto {

    @ApiModelProperty(value = "包间Id")
    private Long roomId;

    @ApiModelProperty(value = "包间图片")
    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String roomPicture;

    @ApiModelProperty(value = "包间名称")
    private String roomName;

    @ApiModelProperty(value = "包间人数")
    private Integer numberOfPeople;

    @ApiModelProperty(value = "包间地址")
    private String roomAddress;

    @ApiModelProperty(value = "包间时间段")
    private List<RoomDetailTimeDto> timeDtos;

    @ApiModelProperty(value = "包间状态")
    private RoomTypeEnum roomTypeEnum;

}
