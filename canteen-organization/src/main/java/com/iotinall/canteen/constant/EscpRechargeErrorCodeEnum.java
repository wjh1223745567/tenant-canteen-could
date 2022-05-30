package com.iotinall.canteen.constant;

import lombok.Getter;

/**
 * escp系统充值接口对接返回错误码
 *
 * @author loki
 * @date 2021/6/22 14:00
 **/
public enum EscpRechargeErrorCodeEnum {

    ILLEGAL_SCHOOL("ILLEGAL_SCHOOL", "企业编码不合法"),
    ILLEGAL_CARD_NAME("ILLEGAL_CARD_NAME", "卡号姓名不匹配"),
    ILLEGAL_CARD("ILLEGAL_CARD", "无效的卡号,卡号不正确"),
    ILLEGAL_STUDENT_NO("ILLEGAL_STUDENT_NO", "无效的工号"),
    ILLEGAL_TEMP_CARD("ILLEGAL_TEMP_CARD", "临时卡，不能签约"),
    ILLEGAL_STATUS_CARD("ILLEGAL_STATUS_CARD", "该卡可能已被注销，冻结或者挂失"),

    ILLEGAL_RECHARGE_AMOUNT("ILLEGAL_RECHARGE_AMOUNT", "充值金额不合法"),
    ILLEGAL_OUT_ID("ILLEGAL_OUT_ID", "无效的代扣请求流水号"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常");

    @Getter
    String code;

    String msg;

    EscpRechargeErrorCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getByCode(String code) {
        try {
            for (EscpRechargeErrorCodeEnum value : EscpRechargeErrorCodeEnum.values()) {
                if (value.code.equals(code)) {
                    return value.msg;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
