package com.iotinall.canteen.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Map;

/**
 *
 * @author xin-bing
 * @date 10/16/2019 11:12
 */
@Getter
@ApiModel(value = "菜品类型 早餐 午餐 晚餐")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MealTypeEnum {
    BREAKFAST(1,"早餐"),
    LUNCH(2,"午餐"),
    DINNER(4,"晚餐"),
    TAKEOUT(8,"网上商城");

    @JsonProperty
    private final int code;
    @JsonProperty
    private final String desc;

    MealTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * json creator方法，解决JsonFormat(shape=Object)的反序列化的坑
     * @param value
     * @return
     */
    @JsonCreator
    public static MealTypeEnum creator(Object value) {
        if(value == null) {
            return null;
        }
        if(value instanceof CharSequence) {
            return MealTypeEnum.valueOf(value.toString());
        }
        Integer code;
        if(value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            code = (Integer) map.get("code");
        } else {
            code = (Integer) value;
        }
        return MealTypeEnum.byCode(code);
    }

    public static MealTypeEnum byCode(int code) {
        for(MealTypeEnum type : MealTypeEnum.values()) {
            if(type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("not found MealTypeEnum by code:"+ code);
    }
}
