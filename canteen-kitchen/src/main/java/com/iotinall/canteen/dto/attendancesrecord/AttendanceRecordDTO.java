package com.iotinall.canteen.dto.attendancesrecord;

import com.iotinall.canteen.dto.employee.EmpListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Data
@ApiModel(description = "考勤记录DTO")
public class AttendanceRecordDTO implements Serializable {
    @ApiModelProperty(value = "考勤记录id")
    private Long id;

    @ApiModelProperty(value = "考勤人员信息")
    private EmpListDTO employee;

    @ApiModelProperty(value = "考勤日期")
    private LocalDate recordDate; // 记录日期

    @ApiModelProperty(value = "考勤记录详情")
    private List<AttendanceShiftRecordDTO> details;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createTime;
}