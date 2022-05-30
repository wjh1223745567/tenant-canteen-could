package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "我的预定---申请人详情显示")
public class MyRoomReserveDetailDto {

    @ApiModelProperty(value = "当前用户姓名")
    private boolean nowEmp;

    @ApiModelProperty(value = "申请人姓名")
    private String empName;

    @ApiModelProperty(value = "申请人所属部门")
    private String orgName;

    @ApiModelProperty(value = "申请人电话")
    private String phone;

    @ApiModelProperty(value = "包间名称")
    private String roomName;

    private String roomAddress;

    @ApiModelProperty(value = "预定餐次")
    private String typeName;

    @ApiModelProperty(value = "预定时间")
    private String dateTime;

    @ApiModelProperty(value = "就餐人数")
    private Integer numberPeople;

    private Integer maxNumberPeople;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "菜品")
    private List<MyRoomReserveDetailMessDto> messDtos;

    @ApiModelProperty(value = "预定状态code")
    private int reserveCode;

    @ApiModelProperty(value = "预定状态")
    private String reserveType;

    @ApiModelProperty(value = "审核意见")
    private String confirmRemark;

    private Long reserveId;

    private Long roomId;

    private Long timeId;

    private LocalDate reservedTime;


}
