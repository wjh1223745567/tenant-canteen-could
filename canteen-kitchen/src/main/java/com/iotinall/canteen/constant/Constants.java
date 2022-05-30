package com.iotinall.canteen.constant;

public interface Constants {
    String ITEM_GROUP_ATTENDANCE = "attendance"; // 考勤

    String ITEM_GROUP_ASSESS_LEVEL = "assess_level"; // 考核类型
    String ITEM_GROUP_RULE_TYPE = "rule_type";  // 规章类型
    String ITEM_GROUP_MORNING_INSPECT = "morning_inspect"; // 晨检类型
    String ITEM_GROUP_DISINFECT_ITEM = "disinfect_item"; // 消毒管理
    String ITEM_GROUP_SAMPLE_ITEM = "sample_item"; // 留样管理
    String ITEM_GROUP_WASH_ITEM = "wash_type"; // 清洗类型
    String ITEM_GROUP_CHOP_ITEM = "chop_type"; // 切配类型
    String ITEM_GROUP_FIRE_PROTECT_ITEM = "fire_protect"; // 消防类型
    String ITEM_GROUP_FACILITY_ITEM = "facility_item"; // 设施类型
    String ITEM_GROUP_KITCHEN_GARBAGE = "kitchen_garbage"; // 厨余垃圾
    String ITEM_GROUP_ENV_ITEM = "env_item"; // 环境卫生
    String ITEM_GROUP_FOOD_ADDITIVES = "food_additives"; // 食品添加剂
    String ITEM_GROUP_COOK = "cook"; // 烹饪

    String[] ITEM_ASSESS_ARRAY = { // 考核类别数组
            ITEM_GROUP_MORNING_INSPECT,
            ITEM_GROUP_DISINFECT_ITEM,
            ITEM_GROUP_SAMPLE_ITEM,
            ITEM_GROUP_WASH_ITEM,
            ITEM_GROUP_CHOP_ITEM,
            ITEM_GROUP_FIRE_PROTECT_ITEM,
            ITEM_GROUP_FACILITY_ITEM,
            ITEM_GROUP_KITCHEN_GARBAGE,
            ITEM_GROUP_ENV_ITEM,
            ITEM_GROUP_FOOD_ADDITIVES,
            ITEM_GROUP_COOK
    };

    /**
     * 考勤状态
     * 0-未打卡 1-正常 2-迟到 3-早退 4-缺卡 8-加班 9-请假  22-严重迟到
     */
    int ATTENDANCE_STATUS_NOT_PUNCH = 0;
    int ATTENDANCE_STATE_NORMAL = 1;
    int ATTENDANCE_STATE_LATE = 2;
    int ATTENDANCE_STATE_LATE_SERIOUS = 22;
    int ATTENDANCE_STATE_EARLY = 3;
    int ATTENDANCE_STATE_MISS = 4;
    int ATTENDANCE_OVERTIME = 8;
    int ATTENDANCE_STATE_VACATE = 9;

    /**
     * 打卡类型
     * 0-上班打卡
     * 1-下班打卡
     */
    int ATTENDANCE_TYPE_IN = 0;
    int ATTENDANCE_TYPE_OUT = 1;

    /**
     * 请假状态
     * 状态 0-等待审批 1-审批通过 2-审批拒绝
     */
    int WAIT_AUDIT = 0;
    int AUDIT_OK = 1;
    int AUDIT_REJECT = 2;
}
