package com.iotinall.canteen.dto.overtime;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkOvertimeApplyCondition {

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * 状态
     * @see com.iotinall.canteen.kitchen.enums.WorkOvertimeApplyStateEnum
     */
    private Integer state;
}
