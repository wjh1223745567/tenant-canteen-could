package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum AttendanceGroupTypeEnum {
    FIXED(0, "固定班制"),
    SHIFT_SYSTEM(1, "排班制");

    AttendanceGroupTypeEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public static AttendanceGroupTypeEnum findByCode(Integer code){
        if(code == null){
            return null;
        }
        for (AttendanceGroupTypeEnum value : AttendanceGroupTypeEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未知类型");
    }

    private final String name;

    private final Integer code;
}
