package com.iotinall.canteen.dto.attendancegroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 添加考勤组
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Data
@ApiModel(description = "添加考勤组")
public class AttendanceGroupAddReq {

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空")
    private String name; // 名称

    @ApiModelProperty(value = "考勤人员列表")
    @NotEmpty(message = "考勤人员不能为空")
    private Set<Long> empList;

    @ApiModelProperty(value = "考勤类型，0固定班制，1排班制", required = true)
    @NotNull(message = "考勤类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "节假日是否自动排休")
    private Boolean holidays;

    @ApiModelProperty(value = "加班规则ID")
    private Long overtimeId;

    @ApiModelProperty(value = "班次")
    private List<GroupShiftReq> shiftList;

    @Data
    @ApiModel(description = "考勤班次")
    @Valid
    public static class GroupShiftReq {
        @ApiModelProperty(value = "周几")
        private Integer weekday;
        @ApiModelProperty(value = "班次id")
        private Long shiftIds;
        @ApiModelProperty(value = "是否启用")
        private Boolean enabled;
    }
}