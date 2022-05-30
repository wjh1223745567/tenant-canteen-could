package com.iotinall.canteen.common.constant;

import com.iotinall.canteen.common.entity.OperationLog;
import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum OperationLogType {

    add(0, "添加"),
    edit(1, "编辑"),
    deleted(2, "删除");

    OperationLogType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OperationLogType findByCode(Integer code){
        for (OperationLogType value : OperationLogType.values()) {
            if(Objects.equals(code, value.getCode())){
                return value;
            }
        }
        throw new BizException("", "未找到操作类型");
    }

    private final Integer code;

    private final String name;
}
