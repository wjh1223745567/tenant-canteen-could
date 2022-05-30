package com.iotinall.canteen.dto.overtime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WorkOvertimeConfigAddReq {

    private Long id;

    /**
     * 规则名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 255,message = "长度不能超过255个字符")
    @ApiModelProperty(value = "规则名称", required = true)
    private String name;

    /**
     * 工作日加班允许加班
     */
    @NotNull(message = "请选择是否允许加班")
    @ApiModelProperty(value = "工作日允许加班")
    private Boolean workOvertimeEnable;

    /**
     * 工作日允许加班
     * @see com.iotinall.canteen.kitchen.enums.WorkOverTypeEnum
     */
    @NotNull(message = "计算方式类型不能为空")
    @Range(min = 0, max = 2, message = "数值只能是0,1,2")
    @ApiModelProperty(value = "工作日计算方式：0,按审批时长算，1，在审批的时段内，按打卡时长算，2无需审批，按打卡时长算")
    private Integer workOverType;

    /**
     * 下班多少分钟后计入加班
     */
    @Range(max = 3000, min = 0, message = "数值范围0~3000")
    @ApiModelProperty(value = "工作日下班多少分钟后计入加班")
    private Integer workOverStart;

    /**
     * 加班时长少于多少分钟不计入加班
     */
    @Range(max = 3000, min = 0, message = "数值范围0~3000")
    @ApiModelProperty(value = "工作日加班时长少于多少分钟不计入加班")
    private Integer workOverLess;

    /**
     * 休息日允许加班类型
     */
    @NotNull(message = "请选择是否允许加班")
    @ApiModelProperty(value = "休息日允许加班")
    private Boolean onRestDaysEnable;

    /**
     * 休息日允许加班类型
     * @see com.iotinall.canteen.kitchen.enums.WorkOverTypeEnum
     */
    @NotNull(message = "计算方式类型不能为空")
    @Range(min = 0, max = 2, message = "数值只能是0,1,2")
    @ApiModelProperty(value = "休息日计算方式：0,按审批时长算，1，在审批的时段内，按打卡时长算，2无需审批，按打卡时长算")
    private Integer onRestDaysType;

    @Range(max = 3000, min = 0, message = "数值范围0~3000")
    @ApiModelProperty(value = "休息日下班多少分钟后计入加班")
    private Integer onRestDaysStart;

    @Range(max = 3000, min = 0, message = "数值范围0~3000")
    @ApiModelProperty(value = "休息日加班时长少于多少分钟不计入加班")
    private Integer onRestDaysLess;

    /**
     * 节假日允许加班
     */
    @NotNull(message = "请选择是否允许加班")
    @ApiModelProperty(value = "节假日允许加班")
    private Boolean onHolidaysEnable;

    /**
     * 节假日允许加班类型
     * @see com.iotinall.canteen.kitchen.enums.WorkOverTypeEnum
     */
    @NotNull(message = "计算方式类型不能为空")
    @Range(min = 0, max = 2, message = "数值只能是0,1,2")
    @ApiModelProperty(value = "节假日计算方式：0,按审批时长算，1，在审批的时段内，按打卡时长算，2无需审批，按打卡时长算")
    private Integer onHolidaysType;

    @Range(max = 3000, min = 0, message = "数值范围0~3000")
    @ApiModelProperty(value = "节假日下班多少分钟后计入加班")
    private Integer onHolidaysStart;

    @Range(max = 3000, min = 0, message = "数值范围0~3000")
    @ApiModelProperty(value = "节假日加班时长少于多少分钟不计入加班")
    private Integer onHolidaysLess;

}
