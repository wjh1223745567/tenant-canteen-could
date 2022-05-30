package com.iotinall.canteen.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Map;

/**
 * @author WJH
 * @date 2019/11/711:41
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderType {

    TOBEPAID(0, "待支付"),
    PAYSUCCESS(1, "支付成功"),
    PAYFAILE(2,"支付失败");

    private Integer code;

    private String name;

    OrderType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @JsonCreator
    public static OrderType creator(Object value) {
        if(value == null) {
            return null;
        }
        if(value instanceof CharSequence) {
            return OrderType.valueOf(value.toString());
        }
        if(value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Integer code = (Integer) map.get("code");
            return OrderType.values()[code]; // code 假定就是
        }
        Integer code = (Integer) value;
        return OrderType.values()[code];
    }
}
