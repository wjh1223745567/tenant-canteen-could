package com.iotinall.canteen.dto.roomreservation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "包间预定")
public class RoomReserveFindDto {

    private Long id;

    @ApiModelProperty(value = "包厢名称")
    private String roomName;

    @ApiModelProperty(value = "预定人")
    private String empName;

    @ApiModelProperty(value = "所属部门")
    private String orgName;

    @ApiModelProperty(value = "预定时间")
    private String reserveTime;

    @ApiModelProperty(value = "预定菜品")
    private List<RoomReserveFindMessDto> messDtos;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态")
    private Integer toExamine;

    private String remark;

    private String confirmRemark;

    private String toExamineName;

    private Integer numberOfPeople;

}
