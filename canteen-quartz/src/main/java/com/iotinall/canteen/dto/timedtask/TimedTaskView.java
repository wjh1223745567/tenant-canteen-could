package com.iotinall.canteen.dto.timedtask;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TimedTaskView {

    private Long id;

    /**
     * 定时任务
     */
    private String name;

    /**
     * 任务类型
     * @see com.iotinall.canteen.constant.JobTypeEnum
     */
    private Integer type;

    /**
     * 时间表达式
     */
    private String corn;

    /**
     * 最后一次执行时间
     */
    private LocalDateTime lastExecuteTime;

    /**
     * 最后一次是否执行成功
     */
    private Boolean lastSuccess;

    private String remark;

}
