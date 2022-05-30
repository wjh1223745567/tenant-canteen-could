package com.iotinall.canteen.constant;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * 充值类型
 *
 * @author xin-bing
 * @date 10/23/2019 17:30
 */
@Getter
@ApiModel(value = "支付类型 0：微信支付 1：支付宝支付")
public enum PayTypeEnum {
    /**
     * 原有：0-微信支付 1-支付宝支付 2-钱包支付 3-后台充值
     * 闸机通行系统同步数据：1-打款充值、2-现金充值、3-微信充值、4-支付宝充值、5-其他
     */
    WECHAT(0, "微信支付"),
    ALIPAY(1, "支付宝支付"),
    WALLETPAY(2, "钱包支付"),
    SYSTEM(3, "后台充值"),
    DK(4, "打款充值"),
    CASH(5, "现金充值"),
    OTHER(6, "其他");

    private final int code;
    private final String desc;

    PayTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayTypeEnum fromCode(int code) {
        switch (code) {
            case 0:
                return WECHAT;
            case 1:
                return ALIPAY;
            default:
                throw new RuntimeException("unsupported code");
        }
    }
}
