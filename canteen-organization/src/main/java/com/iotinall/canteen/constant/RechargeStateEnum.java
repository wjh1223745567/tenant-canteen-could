package com.iotinall.canteen.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iotinall.canteen.common.exception.BizException;
import lombok.Getter;

/**
 * 充值订单和消费记录
 *
 * @author loki
 * @date 2020/04/27 17:34
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RechargeStateEnum {
    NORMAL(1, "正常"),
    CANCEL(2, "作废");

    private final int code;
    private final String desc;

    RechargeStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RechargeStateEnum fromCode(int code) {
        switch (code) {
            case 1:
                return NORMAL;
            case 2:
                return CANCEL;
        }
        throw new BizException("", "未知类型");
    }
}
