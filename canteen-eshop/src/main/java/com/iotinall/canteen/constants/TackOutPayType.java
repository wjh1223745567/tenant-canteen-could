package com.iotinall.canteen.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Map;

/**
 * @author WJH
 * @date 2019/11/2511:51
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum TackOutPayType {

    SERMONEYBAG(0, "钱包支付"), WXPAY(1, "微信支付"), ONSITEPAYMENT(2, "现场支付");

    private Integer code;

    private String name;

    TackOutPayType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static TackOutPayType getByCode(Integer code){
        if(code == null){
            return null;
        }
        return TackOutPayType.values()[code];
    }

    @JsonCreator
    public static TackOutPayType creator(Object value) {
        if(value == null) {
            return null;
        }
        if(value instanceof CharSequence) {
            return TackOutPayType.valueOf(value.toString());
        }
        if(value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            Integer code = (Integer) map.get("code");
            return TackOutPayType.values()[code]; // code 假定就是
        }
        Integer code = (Integer) value;
        return TackOutPayType.values()[code];
    }

}
