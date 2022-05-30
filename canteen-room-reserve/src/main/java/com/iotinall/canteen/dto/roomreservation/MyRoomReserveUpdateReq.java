package com.iotinall.canteen.dto.roomreservation;

import com.iotinall.canteen.dto.roomreservationreserved.RoomReservedProductAddReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "重新提交参数")
public class MyRoomReserveUpdateReq {

    @ApiModelProperty(value = "预定Id")
    @NotNull(message = "请输入订单Id")
    private Long reserveId;

    @ApiModelProperty(value = "包间Id",required = true)
    @NotNull(message = "请输入包间Id")
    private Long roomId;

    @ApiModelProperty(value = "预定时间段Id",required = true)
    @NotNull(message = "请输入时间段Id")
    private Long timeId;

    @ApiModelProperty(value = "预定日期",required = true)
    @NotNull(message = "请选择预定日期")
    private LocalDate reservedTime;

    @ApiModelProperty(value = "就餐人数",required = true)
    @NotNull(message = "请输入就餐人数")
    private Integer numberOfPeople;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "菜品")
    private List<RoomReservedProductAddReq> productAddReqs;

}
