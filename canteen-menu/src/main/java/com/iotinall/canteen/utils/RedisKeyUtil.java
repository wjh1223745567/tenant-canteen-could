package com.iotinall.canteen.utils;

import com.iotinall.canteen.constants.MealTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * @author bingo
 * @date 11/28/2019 16:38
 */
public class RedisKeyUtil {

    private static final DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 获取日用餐统计
     * @param time 时间
     * @return
     */
    public static String hDayEatVitalKey(LocalDateTime time) {
        return "h:eatvital:" + time.format(STANDARD_DATE_FORMATTER);
    }

    /**
     * 获取
     * @param uuid 交易id
     * @return
     */
    public static String getCurrCanteenEmpCountKey(String uuid) {
        return "s:canteen:curremp:" + uuid;
    }

    /**
     * 获取餐厅就餐人数pattern
     * @return
     */
    public static String getCurrCanteenEmpCountPattern() {
        return getCurrCanteenEmpCountKey("*");
    }

    /**
     * 获取产品推荐key
     * @param date 日期 yyyy-MM-dd
     * @param mealTypeEnum 餐饮类型
     * @param productId 食品id
     * @return 返回生成好的key
     */
    public static String getMessProductRecommendKey(String date, MealTypeEnum mealTypeEnum, Long productId) {
        return "prod:recommend:" + date + ":" + mealTypeEnum.getCode() + ":" + productId;
    }

    /**
     * 本周商品推荐次数key
     * @param productId
     * @return
     */
    public static String getProductInWeekRecommendCountKey(Long productId){
        return "prod:recommend:" + LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) + ":" + productId;
    }

    /**
     * 获取产品推荐
     * @param date 日期
     * @param mealTypeEnum 食用类型
     * @return String
     */
    public static String getMessProductRecommendZsetKey(String date, MealTypeEnum mealTypeEnum) {
        return "prod:recommend:" + date + ":" + mealTypeEnum.getCode();
    }
}
