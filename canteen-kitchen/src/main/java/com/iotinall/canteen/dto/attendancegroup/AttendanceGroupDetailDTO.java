package com.iotinall.canteen.dto.attendancegroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "考勤组详情")
public class AttendanceGroupDetailDTO {
    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "名称")
    private String name; // 名称

    @ApiModelProperty(value = "人数")
    private Integer empCount; // 人数

    @ApiModelProperty(value = "排班列表")
    private List<AttendanceGroupDTO.GroupShiftDTO> shifts;

    @ApiModelProperty(value = "考勤员工列表")
    private List<GroupMemberDTO> members;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "加班配置")
    private Long overtimeId;

    @ApiModelProperty(value = "是否")
    private Boolean holidays;

    @Data
    @ApiModel(description = "考勤组员工")
    public static class GroupMemberDTO implements Serializable {
        @ApiModelProperty(value = "员工id")
        private Long id;
        @ApiModelProperty(value = "员工姓名")
        private String name;
    }
}
