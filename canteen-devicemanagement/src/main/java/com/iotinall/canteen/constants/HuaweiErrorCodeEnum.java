package com.iotinall.canteen.constants;

import lombok.Getter;

/**
 * escp系统接口对接返回错误码
 *
 * @author loki
 * @date 2021/6/22 14:00
 **/
public enum HuaweiErrorCodeEnum {

    SUCCESS_0(0, "成功"),
    ERR_UPDATE_PWD_NEEDED(119101308, "用户首次登陆需要修改密码"),
    ERR_PWD_EXPIRE(119101305, "用户密码过期"),
    ERR_UPDATE_PWD_PRE(119101359, "次登录的是预置账号"),
    ERR_DEVICE_NOT_SUPPORT(189108033, "设备不支持"),
    ERR_SNAPSHOT(189146001, "平台抓拍失败"),
    ERR_DOMAIN_CODE_NOT_EXIST(2130000011, "域编码不存在");

    @Getter
    int code;

    String msg;

    HuaweiErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getByCode(Integer code) {
        try {
            for (HuaweiErrorCodeEnum value : HuaweiErrorCodeEnum.values()) {
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
