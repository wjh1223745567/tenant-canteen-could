package com.iotinall.canteen.dto.roomreservationreserved;

import com.iotinall.canteen.common.constant.MealTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "订房详情时间段")
public class RoomDetailTimeDto {

    @ApiModelProperty(value = "时间段Id")
    private Long timeId;

    @ApiModelProperty(value = "餐次 1:早餐，2:午餐，4:晚餐")
    private MealTypeEnum type;

    @ApiModelProperty(value = "早餐时间")
    private String mealTime;

    @ApiModelProperty(value = "节假日是否接受预定")
    private String holiday;

    @ApiModelProperty(value = "可否预定")
    private Boolean reserve;

}
