package com.iotinall.canteen.dto.roomreservation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "包间预定菜品")
public class RoomReserveFindMessDto {

    private Long messId;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String messPicture;

    private String messName;

}
