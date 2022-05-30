package com.iotinall.canteen.dto.roomreservationreserved;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "订房信息显示")
public class RoomReserveAddDto {

    @ApiModelProperty(value = "申请人姓名")
    private String empName;

    @ApiModelProperty(value = "申请人所属部门")
    private String orgName;

    @ApiModelProperty(value = "申请人电话")
    private String phone;

    @ApiModelProperty(value = "包间名称")
    private String roomName;

    @ApiModelProperty(value = "预定餐次")
    private String typeName;

    @ApiModelProperty(value = "预定时间")
    private String dateTime;

}
