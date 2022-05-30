package com.iotinall.canteen.constant;

/**
 * 短信消息类型
 */
public interface PhoneSmsType {

    /**
     * 密码登录
     */
    int SMS_LOGIN = 0;

    /**
     * 密码找回验证码
     */
    int SMS_PASSWORD = 1;

    /**
     * 支付密码修改
     */
    int SMS_PAY_PASSWORD = 2;

}
