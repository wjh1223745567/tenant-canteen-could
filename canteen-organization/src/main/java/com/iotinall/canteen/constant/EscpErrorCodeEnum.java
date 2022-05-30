package com.iotinall.canteen.constant;

import lombok.Getter;

/**
 * escp系统接口对接返回错误码
 *
 * @author loki
 * @date 2021/6/22 14:00
 **/
public enum EscpErrorCodeEnum {

    SUCCESS_200(200, "成功"),
    ERR_100(100, "业务异常"),
    ERR_300(300, "当前应用已被禁用"),
    ERR_301(301, "没有指定的机构和应用"),
    ERR_302(302, "当前机构不是联机模式"),
    ERR_303(303, "没有该接口的访问权限"),
    ERR_401(401, "不支持的请求方式"),
    ERR_402(402, "接口参数不合法"),
    ERR_403(403, "接口重复访问"),
    ERR_404(404, "没有该接口"),
    ERR_410(410, "不支持的加密方式"),
    ERR_411(411, "加解密异常"),
    ERR_420(420, "不支持的签名方式"),
    ERR_421(421, "签名/验签异常"),
    ERR_500(500, "未知异常");

    @Getter
    int code;

    String msg;

    EscpErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getByCode(Integer code) {
        try {
            for (EscpErrorCodeEnum value : EscpErrorCodeEnum.values()) {
                if (value.getCode() == code) {
                    return value.msg;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
