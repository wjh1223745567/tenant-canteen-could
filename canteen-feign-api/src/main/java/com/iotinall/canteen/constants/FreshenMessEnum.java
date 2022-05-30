package com.iotinall.canteen.constants;

import lombok.Getter;

/**
 * 通知页面刷新消息
 */
@Getter
public enum FreshenMessEnum {
    TODAY_DINERS_STAT("today_diners_stat", "今日就餐实况：总人数，总金额"),
    TODAY_STOCK_STAT("today_stock_stat", "今日仓库实况：采购金额，出库金额"),
    CANTEEN_INFO("canteen_info","餐厅环境：温湿度"),
    DINERS_STAT_30("diners_stat_30", "近30日就餐人数趋势"),
    MENU("menu", "早中晚菜谱"),
    MENU_POSITIVE_TOP5("menu_positive_top5", "好评菜谱TOP5"),
    MENU_NEGATIVE_TOP5("menu_negative_top5", "差评菜谱TOP5"),
    MENU_RECOMMEND_TOP5("menu_recommend_top5", "推荐菜谱TOP5"),
    MENU_HOT_SALES("menu_hot_sales", "网上商城-本周新品"),
    WH_INFO("wh_info", "仓库环境：温湿度"),
    MESS_PRODUCT_TRACE_SOURCE("mess_product_trace_source", "早中晚餐菜谱溯源"),
    STOCK_AMOUNT_30("stock_amount_30", "近30日采购/出库金额趋势"),
    STOCK_IN_TOP5("stock_in_top5", "采购TOP5"),
    STOCK_WARN("stock_warn", "库存预警"),
    STOCK_IN_COST_30("stock_in_cost_30", "近30日采购成本"),
    STOCK_ACCEPT_CERT("stock_accept_cert", "验收凭证"),
    KITCHEN_INFO("kitchen_info", "后厨环境：温湿度"),
    KITCHEN_WORKER("kitchen_worker", "后厨人员"),
    KITCHEN_ENV("kitchen_env", "环境卫生"),
    KITCHEN_BRIGHT("kitchen_bright", "AI+明厨亮灶"),
    FEEDBACK("feedback", "反馈列表");


    FreshenMessEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private final String type;

    private final String name;


}
