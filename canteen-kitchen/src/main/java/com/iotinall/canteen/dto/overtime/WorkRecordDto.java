package com.iotinall.canteen.dto.overtime;

import com.iotinall.canteen.constant.DayTypeEnum;
import com.iotinall.canteen.constant.WorkOverTypeEnum;
import com.iotinall.canteen.entity.AttendanceShift;
import lombok.Data;

@Data
public class WorkRecordDto {

    private DayTypeEnum typeEnum;

    private AttendanceShift shift;

    private WorkOverTypeEnum overTypeEnum;
}
