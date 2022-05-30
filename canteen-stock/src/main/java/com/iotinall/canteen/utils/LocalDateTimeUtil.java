package com.iotinall.canteen.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * localdatetime 工具类
 *
 * @author loki
 * @date 2020/04/10 20:07
 */
public class LocalDateTimeUtil {

    /**
     * @author loki
     * @date 2020/05/06 17:54
     */
    public static String localDatetime2Str(LocalDateTime date) {
        return localDatetime2Str(date, DateTimeFormatters.STANDARD_DATE_TIME_FORMATTER);
    }

    /**
     * @author loki
     * @date 2020/05/06 17:54
     */
    public static String localDatetime2Str(LocalDateTime date, DateTimeFormatter formatter) {
        return formatter.format(date);
    }

    /**
     * 字符串转成localDateTime
     *
     * @author loki
     * @date 2020/04/13 10:22
     */
    public static LocalDateTime str2LocalDateTime(String date) {
        if (date.length() <= 10) {
            return LocalDateTime.parse(date, DateTimeFormatters.STANDARD_DATE_FORMATTER);
        } else {
            return LocalDateTime.parse(date, DateTimeFormatters.STANDARD_DATE_TIME_FORMATTER);
        }
    }

    /**
     * 字符串转成localDateTime
     *
     * @author loki
     * @date 2020/04/13 10:22
     */
    public static LocalDateTime str2LocalDateTime(String date, DateTimeFormatter df) {
        return LocalDateTime.parse(date, df);
    }

    /**
     * localDate 转 localDateTime
     *
     * @author loki
     * @date 2020/04/14 11:10
     */
    public static LocalDateTime localDate2LocalDateTime(LocalDate localDate) {
        String date = localDate.toString() + " 00:00:00";
        return LocalDateTime.parse(date, DateTimeFormatters.STANDARD_DATE_FORMATTER);
    }

    /**
     * Date转成localDateTime
     *
     * @author loki
     * @date 2020/04/13 10:22
     */
    public static LocalDateTime long2LocalDateTime(Long date) {
        return LocalDateTime.ofInstant(new Date(date * 1000).toInstant(), ZoneId.systemDefault());
    }
}
