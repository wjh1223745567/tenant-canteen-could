package com.iotinall.canteen.constant;

import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

/**
 * 考核类型
 */
@Getter
public enum AssessRecordTypeEnum {
    MONTHLY(0, "月度"),
    QUARTERLY(1, "季度"),
    YEAR(2, "年度");

    AssessRecordTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static AssessRecordTypeEnum findByCode(Integer code){
        if(code == null){
            return null;
        }

        for (AssessRecordTypeEnum value : AssessRecordTypeEnum.values()) {
            if(Objects.equals(value.getCode(), code)){
                return value;
            }
        }
        throw new BizException("", "未找到考核类型");
    }

    private final int code;

    private final String name;
}
