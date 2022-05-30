package com.iotinall.canteen.dto.overtime;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class WorkOvertimeApplyAddReq {

    private Long id;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @Future(message = "请选择未来的时间")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Future(message = "请选择未来的时间")
    private LocalDateTime endTime;

}
