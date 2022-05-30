package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum WorkOverTypeEnum {
    PROCESSING_TIME(0, "按审批时长计算"),
    CHECK_IN_TIME(1, "在审批的时间段内，按打卡时长计算"),
    NO_APPROVAL(2, "无需审批，按打卡时长计算");

    WorkOverTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static WorkOverTypeEnum findByCode(Integer code){
        if(code == null){
            return null;
        }
        for (WorkOverTypeEnum value : WorkOverTypeEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未找到审批类型");
    }

    private final Integer code;

    private final String name;

}
