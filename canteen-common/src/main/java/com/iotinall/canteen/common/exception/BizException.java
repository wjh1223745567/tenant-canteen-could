package com.iotinall.canteen.common.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 业务错误
 *
 * @author xin-bing
 * @date 10/16/2019 14:51
 */
@Getter
@Setter
@ToString
public class BizException extends RuntimeException {
    private String code;
    private String msg;

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BizException(String msg) {
        super(msg);
        this.code = "";
        this.msg = msg;
    }
}
