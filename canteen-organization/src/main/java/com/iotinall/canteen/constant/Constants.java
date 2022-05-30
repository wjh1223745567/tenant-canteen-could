package com.iotinall.canteen.constant;

public abstract class Constants {
    /**
     * 账单变动说明
     * 1-早餐消费 2-午餐消费 3-晚餐消费 4-打款充值 5-现金充值
     * 6-微信充值 7-支付宝充值 8-其他充值  9-外带外购 10-消费退款 11-充值取消
     */
    public final static int WALLET_BILL_DETAIL_REASON_CONSUME_BREAKFAST = 1;
    public final static int WALLET_BILL_DETAIL_REASON_CONSUME_LUNCH = 2;
    public final static int WALLET_BILL_DETAIL_REASON_CONSUME_DINNER = 3;
    public final static int WALLET_BILL_DETAIL_REASON_RECHARGE_WX = 6;
    public final static int WALLET_BILL_DETAIL_TAKE_OUT = 9;

}
