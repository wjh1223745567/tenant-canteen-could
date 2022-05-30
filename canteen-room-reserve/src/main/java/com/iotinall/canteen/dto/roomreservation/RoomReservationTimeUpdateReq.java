package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RoomReservationTimeUpdateReq {

    @ApiModelProperty(value = "餐次时间Id")
    private Long id;

    @ApiModelProperty(value = "餐次")
    @NotNull(message = "餐次不能为空")
    private int mealType;

    @ApiModelProperty(value = "餐次开始时间")
    @NotBlank(message = "餐次开始时间不能为空")
    private String beginTime;

    @ApiModelProperty(value = "餐次结束时间")
    @NotBlank(message = "餐次结束时间不能为空")
    private String endTime;

    @ApiModelProperty(value = "节假日是否接待 0不营业 1营业")
    @NotNull(message = "请选择节假日是否接待")
    private Boolean holiday;

}
