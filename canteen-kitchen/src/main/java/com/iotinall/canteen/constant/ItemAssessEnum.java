package com.iotinall.canteen.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
@Getter
public enum ItemAssessEnum {

//  String ITEM_GROUP_ATTENDANCE = "attendance";
//    String ITEM_GROUP_ASSESS_LEVEL = "assess_level"; // 考核类型
//    String ITEM_GROUP_RULE_TYPE = "rule_type";  // 规章类型
//    String ITEM_GROUP_MORNING_INSPECT = "morning_inspect"; // 晨检类型
//    String ITEM_GROUP_DISINFECT_ITEM = "disinfect_item"; // 消毒管理
//    String ITEM_GROUP_SAMPLE_ITEM = "sample_item"; // 留样管理
//    String ITEM_GROUP_WASH_ITEM = "wash_type"; // 清洗类型
//    String ITEM_GROUP_CHOP_ITEM = "chop_type"; // 切配类型
//    String ITEM_GROUP_FIRE_PROTECT_ITEM = "fire_protect"; // 消防类型
//    String ITEM_GROUP_FACILITY_ITEM = "facility_item"; // 设施类型
//    String ITEM_GROUP_KITCHEN_GARBAGE = "kitchen_garbage"; // 厨余垃圾
//    String ITEM_GROUP_ENV_ITEM = "env_item"; // 环境卫生
//    String ITEM_GROUP_FOOD_ADDITIVES = "food_additives"; // 食品添加剂
//    String ITEM_GROUP_COOK = "cook"; // 烹饪

    ITEM_GROUP_ATTENDANCE(Constants.ITEM_GROUP_ATTENDANCE, "考勤"),
    ITEM_GROUP_ASSESS_LEVEL(Constants.ITEM_GROUP_ASSESS_LEVEL, "考核类型"),
    ITEM_GROUP_RULE_TYPE(Constants.ITEM_GROUP_RULE_TYPE, "规章类型"),
    ITEM_GROUP_MORNING_INSPECT(Constants.ITEM_GROUP_MORNING_INSPECT, "晨检类型"),
    ITEM_GROUP_DISINFECT_ITEM(Constants.ITEM_GROUP_DISINFECT_ITEM, "消毒管理"),
    ITEM_GROUP_SAMPLE_ITEM(Constants.ITEM_GROUP_SAMPLE_ITEM, "留样管理"),
    ITEM_GROUP_WASH_ITEM(Constants.ITEM_GROUP_WASH_ITEM, "清洗类型"),
    ITEM_GROUP_CHOP_ITEM(Constants.ITEM_GROUP_CHOP_ITEM, "切配类型"),
    ITEM_GROUP_FIRE_PROTECT_ITEM(Constants.ITEM_GROUP_FIRE_PROTECT_ITEM, "消防类型"),
    ITEM_GROUP_FACILITY_ITEM(Constants.ITEM_GROUP_FACILITY_ITEM, "设施类型"),
    ITEM_GROUP_KITCHEN_GARBAGE(Constants.ITEM_GROUP_KITCHEN_GARBAGE, "厨余垃圾"),
    ITEM_GROUP_ENV_ITEM(Constants.ITEM_GROUP_ENV_ITEM, "环境卫生"),
    ITEM_GROUP_FOOD_ADDITIVES(Constants.ITEM_GROUP_FOOD_ADDITIVES, "食品添加剂"),
    ITEM_GROUP_COOK(Constants.ITEM_GROUP_COOK, "烹饪");

    ItemAssessEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;


    public static String getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }

        for (ItemAssessEnum data : ItemAssessEnum.values()) {
            if (data.code.equals(code)) {
                return data.name;
            }
        }

        return "";
    }

}
