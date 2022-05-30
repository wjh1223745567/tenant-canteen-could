package com.iotinall.canteen.common.util;

/**
 * @author WJH
 * @date 2019/11/2914:14
 */
public interface RedisCacheUtil {

    // 字典缓存
    String SYS_DICT = "SYS_DICT";
    // 系统组织表
    String SYS_ORG = "SYS_ORG";
    // 组织员工
    String SYS_ORG_EMP = "SYS_ORG_EMP";

    // 消费设置
    String FIN_CURR_CONSUME_SETTING = "FIN_CURR_CONSUME_SETTING";
    // 消费管理
    String FIN_TRANSACTION_RECORD = "FIN_TRANSACTION_RECORD";
    // 充值记录
    String FIN_RECHARGE_RECORDS = "FIN_RECHARGE_RECORDS";

    // 餐厅
    String MESS = "MESS";

    // 产品
    String MESS_PRODUCT = "MESS_PRODUCT";
    // 菜谱
    String MESS_DAILY_MENU = "MESS_DAILY_MENU";
    String MESS_PRODUCT_RECOMMEND = "MESS_PRODUCT_RECOMMEND";
    // 外卖商品缓存
    String MESS_TAKEOUT_PRODUCT = "MESS_TAKEOUT_PRODUCT";

    // 已激活的人脸设备
    String EQU_ACTIVATED_DEVICE = "EQU_ACTIVATED_DEVICE";
    // 人脸变更记录
    String EQU_FACE_CHANGE_RECORD = "EQU_FACE_CHANGE_RECORD";
    // 临时存储激活的设备，
    String EQU_TMP_ACTIVE_DEVICE = "EQU_TMP_ACTIVE_DEVICE";
    // 摄像头设备
    String EQU_CAMERA = "EQU_CAMERA";

    // 推荐菜谱
    String APP_DAILY_MENU = "APP_DAILY_MENU";
    // 咨询相关
    String INFORMATION_TYPE = "INFORMATION_TYPE";
    String APP_INFORMATION_LIST = "APP_INFORMATION_LIST";
    String appInformationComment = "appInformationComment";

    /**
     * 首页缓存
     */
    String DASHBOARD_CONSUME_STATIS = "DASHBOARD_CONSUME_STATIS";

    String dashboardComparisonDishes = "dashboardComparisonDishes";

    String dashboardEatAllCount = "dashboardEatAllCount";


    /**
     * 外带外购
     */
    String tackOutMessProduct = "tackOutMessProduct";

    String tackOutOutsourcing = "tackOutOutsourcing";

    // APP 个人信息
    String APP_USER_INFO = "APP_USER_INFO";

    // APP_FEEDBACK
    String APP_FEEDBACK = "APP_FEEDBACK";

    // 外带订单
    String TAKEOUT_ORDER = "TAKEOUT_ORDER";

    /**
     * 购物车
     */
    String appShoppingCart = "appShoppingCart";

    /**
     * 系统菜品
     */
    String SYS_DISH_10 = "SYS_DISH_10";
    String SYS_DISH = "SYS_DISH";
    String SYS_MATERIAL_10 = "SYS_MATERIAL_10";
    String SYS_MATERIAL = "SYS_MATERIAL";
    String SYS_CUISINE = "SYS_CUISINE";
    String SYS_FLAVOUR = "SYS_FLAVOUR";
    String SYS_CRAFT = "SYS_CRAFT";

    /**
     * 库存管理
     */
    String STOCK_SUPPLIER_PAGE = "STOCK_SUPPLIER_PAGE";
    String STOCK_SUPPLIER_LIST = "STOCK_SUPPLIER_LIST";
    String STOCK_GOODS_TYPE_PAGE = "STOCK_GOODS_TYPE_PAGE";
    String STOCK_GOODS_TYPE_LIST = "STOCK_GOODS_TYPE_LIST";
    String STOCK_WAREHOUSE_PAGE = "STOCK_WAREHOUSE_PAGE";
    String STOCK_WAREHOUSE_LIST = "STOCK_WAREHOUSE_LIST";
    String STOCK_GOODS_PAGE = "STOCK_GOODS_PAGE";
    String STOCK_IN_OUT_ORDER_PAGE = "STOCK_IN_OUT_ORDER_PAGE";
    String STOCK_WAREHOUSE_STRUCTURE_TREE = "STOCK_WAREHOUSE_STRUCTURE_TREE";
    String STOCK_PAGE = "STOCK_PAGE";
    String STOCK_STRUCTURE_MAP_LIST = "STOCK_STRUCTURE_MAP_LIST";

    /**
     * 流程配置
     */
    String TASK_HANDLERS = "TASK_HANDLERS";

    /**
     * 看板实况
     */
    String TEMPERATURE_HUMIDITY = "TEMPERATURE_HUMIDITY";
}
