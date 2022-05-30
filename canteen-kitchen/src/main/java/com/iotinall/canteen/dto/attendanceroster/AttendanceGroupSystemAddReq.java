package com.iotinall.canteen.dto.attendanceroster;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 排班
 */
@Data
@ApiModel(value = "排班制")
public class AttendanceGroupSystemAddReq {

    @NotNull(message = "班次组ID不能为空")
    @ApiModelProperty(value = "班次组ID", required = true)
    private Long groupId;

    @ApiModelProperty(value = "人员ID", required = true)
    @NotNull(message = "人员ID不能为空")
    private Long employeeId;

    @ApiModelProperty(value = "人员姓名")
    private String employeeName;

    @ApiModelProperty(value = "排班信息")
    @NotEmpty(message = "排班不能为空")
    private List<SystemDate> dateList;

    @Data
    @ApiModel(value = "排班信息")
    @Valid
    public static class SystemDate{

        @NotNull(message = "班次日期不能为空")
        @ApiModelProperty(value = "班次日期:yyyy-MM-dd", required = true)
        private LocalDate date;

        @ApiModelProperty(value = "班次ID，为空是休息")
        private Long shiftId;

    }

}
