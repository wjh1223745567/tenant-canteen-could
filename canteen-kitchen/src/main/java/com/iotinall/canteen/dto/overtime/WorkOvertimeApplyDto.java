package com.iotinall.canteen.dto.overtime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class WorkOvertimeApplyDto {

    private Long id;

    /**
     * 加班人
     */
    @ApiModelProperty("加班人")
    private String employeeName;

    @ApiModelProperty("职位")
    private String position;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    private String auditOpinion;

    /**
     * 状态
     * @see com.iotinall.canteen.kitchen.enums.WorkOvertimeApplyStateEnum
     */
    @ApiModelProperty("状态：0, 待审核，1, 通过，2, 拒绝，6, 过期，7,取消")
    private Integer state;

    @ApiModelProperty(value = "是否是自己的数据")
    private Boolean used;

    private String stateName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
