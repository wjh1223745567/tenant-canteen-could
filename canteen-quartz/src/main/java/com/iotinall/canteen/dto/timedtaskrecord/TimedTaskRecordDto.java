package com.iotinall.canteen.dto.timedtaskrecord;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TimedTaskRecordDto {

    private Long id;

    /**
     * 定时任务名称
     */
    private String timedTaskName;

    /**
     * 执行开始时间
     */
    private LocalDateTime createTime;

    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否执行成功
     */
    private Boolean success;

    /**
     * 执行失败日志
     */
    private String errorLog;

}
