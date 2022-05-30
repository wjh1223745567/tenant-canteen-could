package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum DayTypeEnum {
    WORKING_DAY(0, "工作日"),
    PLAY_DAY(1, "休息日"),
    HOLIDAYS(2, "节假日");

    DayTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DayTypeEnum findByCode(Integer code){
        if(code == null){
            return null;
        }
        for (DayTypeEnum value : DayTypeEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未找到天类型");
    }

    private final Integer code;

    private final String name;
}
