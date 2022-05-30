package com.iotinall.canteen.dto.overtime;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkOvertimeRecordCondition {

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
