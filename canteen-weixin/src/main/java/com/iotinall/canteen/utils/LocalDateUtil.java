package com.iotinall.canteen.utils;

import com.google.common.collect.Lists;
import com.iotinall.canteen.constants.DateTimeFormatters;
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

}
