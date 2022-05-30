package com.iotinall.canteen.dto.attendancesrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.iotinall.canteen.common.util.ImgPair;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalTime;

@ApiModel(value = "打卡记录")
@Data
public class AttendanceShiftRecordDTO {
    private Long id;

    private Long shiftId;

    private String shiftName;

    private LocalTime shiftBeginTime;

    private LocalTime shiftEndTime;

    private LocalTime punchInTime;

    private Integer punchInStatus;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String punchInImg;

    private LocalTime punchOutTime;

    private Integer punchOutStatus;

    @JsonSerialize(using = ImgPair.ImgSerializer.class)
    private String punchOutImg;

    private Long recordId;
}
