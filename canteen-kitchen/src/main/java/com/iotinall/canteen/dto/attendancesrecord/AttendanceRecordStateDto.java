package com.iotinall.canteen.dto.attendancesrecord;

import lombok.Data;

/**
 * 状态统计
 */
@Data
public class AttendanceRecordStateDto {

    private Long empId;

    /**
     * 迟到
     */
    private Integer inLater;

    private Integer outLater;

    /**
     * 早退
     */
    private Integer inEarly;

    private Integer outEarly;

    /**
     * 缺卡
     */
    private Integer inMiss;

    private Integer outMiss;

    /**
     * 严重迟到
     */
    private Integer inLaterSer;

    private Integer outLaterSer;

}
