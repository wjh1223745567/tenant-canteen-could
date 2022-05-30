package com.iotinall.canteen.constant;

import lombok.Getter;

import java.util.Arrays;

public enum AttendanceRecordStatusEnum {
    /**
     * 考勤状态
     * 0-未打卡 1-正常 2-迟到 3-早退 4-缺卡 9-请假
     */
//    int ATTENDANCE_STATUS_NOT_PUNCH = 0;
//    int ATTENDANCE_STATE_NORMAL = 1;
//    int ATTENDANCE_STATE_LATE = 2;
//    int ATTENDANCE_STATE_EARLY = 3;
//    int ATTENDANCE_STATE_MISS = 4;
//    int ATTENDANCE_STATE_VACATE = 9;

    ATTENDANCE_STATUS_NOT_PUNCH(Constants.ATTENDANCE_STATUS_NOT_PUNCH, "未打卡"),
    ATTENDANCE_STATE_NORMAL(Constants.ATTENDANCE_STATE_NORMAL, "正常"),
    ATTENDANCE_STATE_LATE(Constants.ATTENDANCE_STATE_LATE, "迟到"),
    ATTENDANCE_STATE_LATE_SERIOUS(Constants.ATTENDANCE_STATE_LATE_SERIOUS, "严重迟到"),
    ATTENDANCE_STATE_EARLY(Constants.ATTENDANCE_STATE_EARLY, "早退"),
    ATTENDANCE_STATE_MISS(Constants.ATTENDANCE_STATE_MISS, "缺卡"),
    ATTENDANCE_OVERTIME(Constants.ATTENDANCE_OVERTIME, "加班"),
    ATTENDANCE_STATE_VACATE(Constants.ATTENDANCE_STATE_VACATE, "请假");

    @Getter
    private int code;

    @Getter
    private String value;

    AttendanceRecordStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String findValueByCode(int code) {
        return Arrays.stream(AttendanceRecordStatusEnum.values()).filter(item -> item.getCode() == code).findAny().get().getValue();
    }
}
