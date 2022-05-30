package com.iotinall.canteen.dto.overtime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class WorkOvertimeRecordDto {

    private Long id;

    /**
     * 加班人
     */
    @ApiModelProperty(value = "加班人")
    private String employeeName;

    @ApiModelProperty(value = "职位")
    private String position;

    /**
     * 班组信息
     */
    @ApiModelProperty(value = "班组名称")
    private String groupName;

    /**
     * 加班开始时间
     */
    @ApiModelProperty(value = "加班开始时间")
    private LocalDateTime startTime;

    /**
     * 加班结束时间
     */
    @ApiModelProperty(value = "加班结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "加班时长")
    private Long overtime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
