package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

/**
 * 明厨亮灶违规类型
 *
 * @author loki
 * @date 2021/6/24 13:48
 **/
@Getter
public enum KitchenBrightAlarmTypeEnum {

    WORK_CLOTHES_ALARM("WorkClothesAlarm", "未穿工作服检测"),
    TELEPHONING_ALARM("TelephoningAlarm", "打电话检测"),
    SMOKING_ALARM("SmokingAlarm", "吸烟检测"),
    FAST_MOVING("FastMoving", "移动检测"),
    CHEF_HAT_ALARM("ChefHatAlarm", "未戴厨师帽检测");

    KitchenBrightAlarmTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private final String code;

    private final String name;

    public static KitchenBrightAlarmTypeEnum findByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (KitchenBrightAlarmTypeEnum value : KitchenBrightAlarmTypeEnum.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value;
            }
        }
        throw new BizException("", "未找到检测类型");
    }

    public static String getNameFindByCode(String code) {
        if (code == null) {
            return null;
        }
        for (KitchenBrightAlarmTypeEnum value : KitchenBrightAlarmTypeEnum.values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value.getName();
            }
        }
        throw new BizException("", "未找到检测类型");
    }
}
