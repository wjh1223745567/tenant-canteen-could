package com.iotinall.canteen.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

import java.util.Map;

@Getter
@ApiModel(value = "包间状态")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RoomTypeEnum {

    FREE(0, "有空闲"),
    RESERVED(1,"不可预定");

    @JsonProperty
    private final int code;

    @JsonProperty
    private final String desc;

    RoomTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static RoomTypeEnum creator(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof CharSequence) {
            return RoomTypeEnum.valueOf(value.toString());
        }
        Integer code;
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;
            code = (Integer) map.get("code");
        }else {
            code = (Integer) value;
        }
        return RoomTypeEnum.byCode(code);
    }

    public static RoomTypeEnum byCode(int code){
        for (RoomTypeEnum type : RoomTypeEnum.values()) {
            if(type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("not found RoomTypeEnum by code:"+ code);
    }

}
