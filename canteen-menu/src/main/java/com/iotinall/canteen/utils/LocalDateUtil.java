package com.iotinall.canteen.utils;

import com.google.common.collect.Lists;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * localDate 工具类
 *
 * @author loki
 * @date 2020/04/15 10:33
 */
public class LocalDateUtil {
    /**
     * 获取本周开始时间和结束时间
     *
     * @author loki
     * @date 2020/04/28 22:01
     */
    public static List<LocalDate> getCurrentWeekDate() {
        LocalDate inputDate = LocalDate.now();
        TemporalAdjuster FIRST_OF_WEEK =
                TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        TemporalAdjuster LAST_OF_WEEK =
                TemporalAdjusters.ofDateAdjuster(localDate -> localDate.plusDays(DayOfWeek.SUNDAY.getValue() - localDate.getDayOfWeek().getValue()));

        return Lists.newArrayList(inputDate.with(FIRST_OF_WEEK), inputDate.with(LAST_OF_WEEK));
    }
}
