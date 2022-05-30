package com.iotinall.canteen.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Map;

/**
 * @author xin-bing
 * @date 10/16/2019 11:12
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ToExamineEnum {
    SUCCESS(0, "已成功"),
    CANCEL(1, "已取消"),
    BEOVERDUE(2, "已过期");

    @JsonProperty
    private final int code;
    @JsonProperty
    private final String desc;

    ToExamineEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * json creator方法，解决JsonFormat(shape=Object)的反序列化的坑
     *
     * @param value
     * @return
     */
    @JsonCreator
    public static ToExamineEnum creator(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return ToExamineEnum.valueOf(value.toString());
        }
        Integer code;
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            code = (Integer) map.get("code");
        } else {
            code = (Integer) value;
        }
        return ToExamineEnum.byCode(code);
    }

    public static ToExamineEnum byCode(int code) {
        for (ToExamineEnum type : ToExamineEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("not found MealTypeEnum by code:" + code);
    }
}
