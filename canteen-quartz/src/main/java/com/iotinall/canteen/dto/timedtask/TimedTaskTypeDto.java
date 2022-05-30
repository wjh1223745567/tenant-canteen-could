package com.iotinall.canteen.dto.timedtask;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TimedTaskTypeDto {

    private Integer code;

    private String name;

}
