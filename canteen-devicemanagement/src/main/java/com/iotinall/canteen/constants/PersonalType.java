package com.iotinall.canteen.constants;

import lombok.Getter;

import java.util.Objects;

/**
 * 人员类型
 */
@Getter
public enum PersonalType {

    WORKER(0, "单位职工"),
    BACKKITCHEN(1, "后厨人员"),
    OTHER(2,"其他人员");

    PersonalType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private final Integer code;

    private final String name;

    public static PersonalType getByCode(Integer code){
        if(Objects.isNull(code)){
            return null;
        }
        return PersonalType.values()[code];
    }
}