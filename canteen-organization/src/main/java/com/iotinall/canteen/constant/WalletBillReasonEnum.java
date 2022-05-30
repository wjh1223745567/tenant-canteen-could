package com.iotinall.canteen.constant;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * 余额变动类型
 *
 * @author loki
 * @date 2021/01/19 9:43
 */
@Getter
@ApiModel(value = "1-早餐消费 2-午餐消费 3-晚餐消费 4-打款充值 5-现金充值 6-微信充值 7-支付宝充值 8-其他充值 9-网上商城 10-消费退款 11-充值取消退款")
public enum WalletBillReasonEnum {

    /**
     * 账单说明
     */
    BREAKFAST(1, "早餐消费"),
    LUNCH(2, "午餐消费"),
    DINER(3, "晚餐消费"),
    DK(4, "打款充值"),
    CASH(5, "现金充值"),
    WX(6, "微信充值"),
    ALIPAY(7, "支付宝充值"),
    OTHER(8, "其他充值"),
    TAKE_OUT(9, "网上商城"),
    CONSUME_REFUND(10, "消费退款"),
    RECHARGE_CANCEL(11, "充值取消退款");

    private Integer code;

    private String name;

    WalletBillReasonEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getByCode(Integer code) {
        for (WalletBillReasonEnum t : WalletBillReasonEnum.values()) {
            if (code.intValue() == t.getCode().intValue()) {
                return t.getName();
            }
        }
        return "";
    }
}
