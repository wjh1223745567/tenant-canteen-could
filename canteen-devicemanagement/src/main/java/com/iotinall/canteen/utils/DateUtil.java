package com.iotinall.canteen.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * date 工具类
 *
 * @author loki
 * @date 2020/04/10 20:07
 */
public class DateUtil {
    /**
     * LocalDatetime 转 字符串
     *
     * @param date
     * @param formatter
     * @return
     */
    public static String localDatetime2Str(LocalDateTime date, DateTimeFormatter formatter) {
        return formatter.format(date);
    }

    /**
     * LocalDate 转 字符串
     *
     * @param date
     * @param formatter
     * @return
     */
    public static String localDate2Str(LocalDate date, DateTimeFormatter formatter) {
        return date.format(formatter);
    }
}
