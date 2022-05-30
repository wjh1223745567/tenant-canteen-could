package com.iotinall.canteen.utils;

import com.google.common.collect.Lists;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * localDate 工具类
 *
 * @author loki
 * @date 2020/04/15 10:33
 */
public class LocalDateUtil {
    /**
     * @author loki
     * @date 2020/05/15 9:16
     */
    public static LocalDate strToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatters.STANDARD_DATE_FORMATTER);
    }

    /**
     * date 为空,返回今日
     *
     * @author loki
     * @date 2020/04/10 20:09
     */
    public static LocalDate getQueryDate(LocalDate date) {
        if (null == date) {
            return LocalDate.now();
        }
        return date;
    }

    /**
     * 获取两个日期相差的年,月,日,周
     *
     * @author loki
     * @date 2020/04/15 10:34
     */
    public static Integer getUntil(LocalDate start, LocalDate end, ChronoUnit unit) {
        Long until = start.until(end, unit);
        return until.intValue();
    }

    /**
     * 计算两个时间内的每一天
     *
     * @Author loki
     * @Date 2019/11/6 11:41 AM
     **/
    public static List<BetweenDate> getBetweenDateContains(LocalDate beginDate, LocalDate endDate) {
        Long daysNum = getBetweenDays(beginDate, endDate);
        String day;
        String month;
        String monthAndDay;
        LocalDate date;
        BetweenDate betweenDate = new BetweenDate();
        betweenDate.setDate(beginDate);
        List<BetweenDate> betweenDateList = new ArrayList<>(31);
        betweenDateList.add(betweenDate);
        for (int i = 1; i <= daysNum.intValue(); i++) {
            date = beginDate.plusDays(i);
            month = date.getMonthValue() + "";
            day = date.getDayOfMonth() + "";
            monthAndDay = (month.length() == 1 ? ("0" + month) : month) + "/" + (day.length() == 1 ? ("0" + day) : day); //补0
            betweenDate = new BetweenDate();
            betweenDate.setLabel(monthAndDay);
            betweenDate.setDate(date);
            betweenDateList.add(betweenDate);
        }
        return betweenDateList;
    }

    /**
     * 计算两个时间内的每一天
     *
     * @Author loki
     * @Date 2019/11/6 11:41 AM
     **/
    public static List<BetweenDate> getBetweenDate(LocalDate beginDate, LocalDate endDate) {
        Long daysNum = getBetweenDays(beginDate, endDate);
        String day;
        String month;
        String monthAndDay;
        LocalDate date;
        BetweenDate betweenDate;
        List<BetweenDate> betweenDateList = new ArrayList<>(10);
        for (int i = 1; i <= daysNum.intValue(); i++) {
            date = beginDate.plusDays(i);
            month = date.getMonthValue() + "";
            day = date.getDayOfMonth() + "";
            monthAndDay = (month.length() == 1 ? ("0" + month) : month) + "/" + (day.length() == 1 ? ("0" + day) : day); //补0
            betweenDate = new BetweenDate();
            betweenDate.setLabel(monthAndDay);
            betweenDate.setDate(date);
            betweenDateList.add(betweenDate);
        }
        return betweenDateList;
    }

    public static List<BetweenDate> getAllBetweenDate(LocalDate beginDate, LocalDate endDate) {
        Long daysNum = getBetweenDays(beginDate, endDate);
        String day;
        String month;
        String monthAndDay;
        LocalDate date;
        BetweenDate betweenDate;
        List<BetweenDate> betweenDateList = new ArrayList<>(10);
        for (int i = 0; i <= daysNum.intValue(); i++) {
            date = i == 0 ? beginDate : beginDate.plusDays(i);
            month = date.getMonthValue() + "";
            day = date.getDayOfMonth() + "";
            monthAndDay = (month.length() == 1 ? ("0" + month) : month) + "/" + (day.length() == 1 ? ("0" + day) : day); //补0
            betweenDate = new BetweenDate();
            betweenDate.setLabel(monthAndDay);
            betweenDate.setDate(date);
            betweenDateList.add(betweenDate);
        }
        return betweenDateList;
    }

    /**
     * 获取两个日期之间相差的天数
     *
     * @author loki
     * @date 2020/05/02 17:40
     */
    public static Long getBetweenDays(LocalDate beginDate, LocalDate endDate) {
        return endDate.toEpochDay() - beginDate.toEpochDay();
    }

    @Data
    public static class BetweenDate {
        private String label;
        private LocalDate date;
    }

    /**
     * 格式转换
     *
     * @author loki
     * @date 2020/04/16 17:23
     */
    public static String format(LocalDate date) {
        return date.format(DateTimeFormatters.STANDARD_DATE_FORMATTER_);
    }

    /**
     * 格式转换
     *
     * @author loki
     * @date 2020/04/16 17:23
     */
    public static String format(LocalDate date, DateTimeFormatter format) {
        return date.format(format);
    }

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
