package com.iotinall.canteen.dto.attendancevacaterecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;


/**
* @author xinbing
* @date 2020-07-14 16:29:49
*/
@Data
@ApiModel(description = "查询请假记录条件")
public class AttendanceVacateRecordCriteria{

    @ApiModelProperty(value = "状态")
    private Integer state; // 状态

    @ApiModelProperty(value = "审核人")
    private String keyword; // 审核人

    @ApiModelProperty(value = "开始时间")
    private LocalDate beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDate endTime;
}