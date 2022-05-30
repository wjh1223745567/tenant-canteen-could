package com.iotinall.canteen.dto.attendancesrecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Data
@ApiModel(description = "查询考勤记录条件")
public class AttendanceRecordCriteria implements Serializable {

    @ApiModelProperty(value = "员工id")
    private Long empId;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginTime;

    @ApiModelProperty(value = "结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty(value = "考勤状态")
    private Integer status;
}