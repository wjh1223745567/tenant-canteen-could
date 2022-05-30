package com.iotinall.canteen.dto.overtime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WorkOvertimeConfigDto {


    private Long id;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称", required = true)
    private String name;

    /**
     * 工作日加班允许加班
     */
    @ApiModelProperty(value = "工作日允许加班")
    private Boolean workOvertimeEnable;

    /**
     * 工作日允许加班
     *
     * @see com.iotinall.canteen.kitchen.enums.WorkOverTypeEnum
     */
    @ApiModelProperty(value = "工作日计算方式：0,按审批时长算，1，在审批的时段内，按打卡时长算，2无需审批，按打卡时长算")
    private Integer workOverType;

    /**
     * 下班多少分钟后计入加班
     */
    @ApiModelProperty(value = "工作日下班多少分钟后计入加班")
    private Integer workOverStart;

    /**
     * 加班时长少于多少分钟不计入加班
     */
    @ApiModelProperty(value = "工作日加班时长少于多少分钟不计入加班")
    private Integer workOverLess;

    /**
     * 休息日允许加班类型
     */
    @ApiModelProperty(value = "休息日允许加班")
    private Boolean onRestDaysEnable;

    /**
     * 休息日允许加班类型
     *
     * @see com.iotinall.canteen.kitchen.enums.WorkOverTypeEnum
     */
    @ApiModelProperty(value = "休息日计算方式：0,按审批时长算，1，在审批的时段内，按打卡时长算，2无需审批，按打卡时长算")
    private Integer onRestDaysType;

    @ApiModelProperty(value = "休息日下班多少分钟后计入加班")
    private Integer onRestDaysStart;

    @ApiModelProperty(value = "休息日加班时长少于多少分钟不计入加班")
    private Integer onRestDaysLess;

    /**
     * 节假日允许加班
     */
    @ApiModelProperty(value = "节假日允许加班")
    private Boolean onHolidaysEnable;

    /**
     * 节假日允许加班类型
     *
     * @see com.iotinall.canteen.kitchen.enums.WorkOverTypeEnum
     */
    @ApiModelProperty(value = "节假日计算方式：0,按审批时长算，1，在审批的时段内，按打卡时长算，2无需审批，按打卡时长算")
    private Integer onHolidaysType;

    @ApiModelProperty(value = "节假日下班多少分钟后计入加班")
    private Integer onHolidaysStart;

    @ApiModelProperty(value = "节假日加班时长少于多少分钟不计入加班")
    private Integer onHolidaysLess;

}
