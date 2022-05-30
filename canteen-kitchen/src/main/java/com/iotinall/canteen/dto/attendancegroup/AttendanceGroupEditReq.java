package com.iotinall.canteen.dto.attendancegroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 修改考勤组
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Data
@ApiModel(description = "修改考勤组")
public class AttendanceGroupEditReq {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "id不能为空")
    private Long id; // 主键

    @ApiModelProperty(value = "名称")
    private String name; // 名称

    @ApiModelProperty(value = "考勤人员列表")
    @NotEmpty(message = "考勤人员不能为空")
    private Set<Long> empList;

    @ApiModelProperty(value = "考勤类型，0固定班制，1排班制", required = true)
    @NotNull(message = "考勤类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "加班规则ID")
    private Long overtimeId;

    @ApiModelProperty(value = "节假日是否自动排休")
    private Boolean holidays;

    @ApiModelProperty(value = "班次")
    private List<AttendanceGroupAddReq.GroupShiftReq> shiftList;
}