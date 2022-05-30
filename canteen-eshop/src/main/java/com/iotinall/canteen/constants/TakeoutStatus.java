package com.iotinall.canteen.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

/**
 * 外带订单状态
 * @author WJH
 * @date 2019/11/2211:57
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TakeoutStatus {
    UNPAID("待支付"), // 0
    PAID("已支付"), // 1
    COMPLETED("已完成"), // 2
    CANCEL("已取消"), // 3
    EXPIRED("已过期"); // 4

    @JsonProperty
    private final Integer code = this.ordinal();

    @JsonProperty
    private final String desc;

    TakeoutStatus(String desc) {
        this.desc = desc;
    }

    public static TakeoutStatus findByCode(Integer code){
        try {
            return TakeoutStatus.values()[code];
        } catch (Exception e) {
            return null;
        }
    }

    @JsonCreator
    public static TakeoutStatus creator(Object value) {
        if(value == null) {
            return null;
        }
        if(value instanceof CharSequence) {
            return TakeoutStatus.valueOf(value.toString());
        }
        if(value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Integer code = (Integer) map.get("code");
            return TakeoutStatus.values()[code]; // code 假定就是
        }
        Integer code = (Integer) value;
        return TakeoutStatus.values()[code];
    }

    public static TakeoutStatus getByCode(Integer code) {
        if(code == null) {
            return null;
        }
        if(code >= values().length){
            return null;
        }
        TakeoutStatus[] values = TakeoutStatus.values();
        return values[code];
    }
}
