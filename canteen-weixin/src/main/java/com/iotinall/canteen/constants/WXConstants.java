package com.iotinall.canteen.constants;


/**
 * 微信配置
 *
 * @author loki
 * @date 2020/09/23 18:09
 */
public class WXConstants {

    public static final String SEND_MINI_PROGRAM_SUB_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    /**
     * 消息消息配置
     * 1-早餐
     * 2-午餐
     * 3-晚餐
     * 4-余额不足
     * 5-点评和饮食记录
     * 6-商城取货
     * 7-反馈
     * 8-进销存采购入库申请
     * 9-进销存采购退货申请
     * 10-进销存领用出库申请
     * 11-进销存领用退库申请
     * 12-进销存采购入库审核
     * 13-进销存采购退货审核
     * 14-进销存领用出库审核
     * 15-进销存领用退库审核
     * 16-商城截止订购提醒
     */
    public static final int MSG_CONTENT_TYPE_BREAKFAST = 1;
    public static final int MSG_CONTENT_TYPE_LUNCH = 2;
    public static final int MSG_CONTENT_TYPE_DINNER = 3;
    public static final int MSG_CONTENT_TYPE_BALANCE = 4;
    public static final int MSG_CONTENT_TYPE_COMMENTS = 5;
    public static final int MSG_CONTENT_TYPE_SHOPPING_PICKUP = 6;
    public static final int MSG_CONTENT_TYPE_FEEDBACK = 7;
    public static final int MSG_CONTENT_TYPE_STOCK_IN_APPLY = 8;
    public static final int MSG_CONTENT_TYPE_STOCK_IN_BACK_APPLY = 9;
    public static final int MSG_CONTENT_TYPE_STOCK_OUT_APPLY = 10;
    public static final int MSG_CONTENT_TYPE_STOCK_OUT_BACK_APPLY = 11;
    public static final int MSG_CONTENT_TYPE_STOCK_IN_APPLY_AUDIT = 12;
    public static final int MSG_CONTENT_TYPE_STOCK_IN_BACK_APPLY_AUDIT = 13;
    public static final int MSG_CONTENT_TYPE_STOCK_OUT_APPLY_AUDIT = 14;
    public static final int MSG_CONTENT_TYPE_STOCK_OUT_BACK_APPLY_AUDIT = 15;
    public static final int MSG_CONTENT_TYPE_SHOPPING_DEADLINE = 16;

    /**
     * 消息模板类型
     */
    public static final String TEMPLATE_STOCK_BILL_APPLY = "STOCK_BILL_APPLY";
    public static final String TEMPLATE_STOCK_BILL_AUDIT = "STOCK_BILL_AUDIT";
    public static final String TEMPLATE_DINER_BEGIN = "DINER_BEGIN";
    public static final String TEMPLATE_SHOPPING_PICKUP = "SHOPING_PICKUP";
    public static final String TEMPLATE_BALANCE_NOT_ENOUGH = "BALANCE_NOT_ENOUGH";
    public static final String TEMPLATE_INFORMATION_NOTICE = "INFORMATION_NOTICE";
    public static final String TEMPLATE_FEEDBACK_NOTICE = "FEEDBACK";
    public static final String TEMPLATE_SHOPPING_DEADLINE = "SHOPING_DEADLINE";

    /**
     * 跳转小程序页面
     */
    public static final String PAGE_STOCK_IN_AUDIT = StockConstants.BILL_TYPE.STOCK_IN;
    public static final String PAGE_STOCK_IN_BACK_AUDIT = StockConstants.BILL_TYPE.STOCK_IN_BACK;
    public static final String PAGE_STOCK_OUT_AUDIT = StockConstants.BILL_TYPE.STOCK_OUT;
    public static final String PAGE_STOCK_OUT_BACK_AUDIT_PAGE = StockConstants.BILL_TYPE.STOCK_OUT_BACK;
    public static final String PAGE_DINER_BEGIN = TEMPLATE_DINER_BEGIN;
    public static final String PAGE_SHOPING_PICKUP = TEMPLATE_SHOPPING_PICKUP;
    public static final String PAGE_BALANCE_NOT_ENOUGH = TEMPLATE_BALANCE_NOT_ENOUGH;
    public static final String PAGE_FEEDBACK = TEMPLATE_FEEDBACK_NOTICE;
}
