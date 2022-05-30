package com.iotinall.canteen.common.protocol;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xin-bing
 * @date 10/16/2019 14:07
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ResultDTO<T> {
    private String code;
    private String msg;
    private T data;

    private ResultDTO(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultDTO() {
    }

    public static <T> ResultDTO<T> success(T data, String msg) {
        return new ResultDTO<>("0", msg, data);
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<>("0", null, data);
    }

    public static <T> ResultDTO<T> success() {
        return new ResultDTO<>("0", null, null);
    }

    public static <T> ResultDTO<T> failed(String code, String msg) {
        return new ResultDTO<>(code, msg, null);
    }

    public static <T> ResultDTO<T> failed(String msg) {
        return new ResultDTO<>("", msg, null);
    }
}
