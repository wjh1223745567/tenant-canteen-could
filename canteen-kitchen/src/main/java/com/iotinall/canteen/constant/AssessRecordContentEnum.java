package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

/**
 * 考核内容分类
 */
@Getter
public enum AssessRecordContentEnum {

    ATTENDANCE(0, "考勤"),
    MORNING_CHECK(1, "晨检管理"),
    RESERVE_SAMPLE(2, "留样管理"),
    DISINFECTION(3, "消毒管理"),
    FOOD_WASTE(4, "餐厨垃圾"),
    EQUIPMENT(5, "设备设施"),
    ENVIRONMENT(6, "环境卫生");

    AssessRecordContentEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static AssessRecordContentEnum findByCode(Integer code){
        if(code == null){
            return null;
        }
        for (AssessRecordContentEnum value : AssessRecordContentEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未知考核内容");
    }

    private final Integer code;

    private final String name;

}
