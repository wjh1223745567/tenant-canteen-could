package com.iotinall.canteen.dto.attendancegroup;

import com.iotinall.canteen.dto.attendanceshift.AttendanceShiftDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author xinbing
 * @date 2020-07-13 20:28:37
 */
@Data
@ApiModel(description = "考勤组DTO")
public class AttendanceGroupDTO implements Serializable {

    @ApiModelProperty(value = "主键", required = true)
    private Long id; // 主键

    @ApiModelProperty(value = "名称")
    private String name; // 名称

    @ApiModelProperty(value = "人数")
    private Integer empCount; // 人数

    @ApiModelProperty(value = "排班列表")
    private List<GroupShiftDTO> shiftList;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "加班配置")
    private Long overtimeId;

    @ApiModelProperty(value = "是否")
    private Boolean holidays;

    @Data
    @ApiModel(description = "考勤组班次")
    public static class GroupShiftDTO implements Serializable {
        @ApiModelProperty(value = "星期1~7")
        private Integer weekday;
        @ApiModelProperty(value = "排班列表")
        private AttendanceShiftDTO details;
    }
}