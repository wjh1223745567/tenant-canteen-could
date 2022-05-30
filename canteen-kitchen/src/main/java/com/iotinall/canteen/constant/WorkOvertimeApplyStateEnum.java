package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum WorkOvertimeApplyStateEnum {
    PENDING(0, "待审核"),
    SUCCESS(1, "通过"),
    REFUSE(2, "拒绝"),
    EXPIRED(6, "过期"),
    CANCEL(7, "取消");

    WorkOvertimeApplyStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static WorkOvertimeApplyStateEnum findByCode(Integer code){
        if(code == null){
            return null;
        }

        for (WorkOvertimeApplyStateEnum value : WorkOvertimeApplyStateEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未找到审批类型");
    }


    private final Integer code;

    private final String name;

}
