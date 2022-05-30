package com.iotinall.canteen.util;

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
     * 字符串转成localDateTime
     *
     * @author loki
     * @date 2020/04/13 10:22
     */
    public static LocalDateTime str2LocalDateTimeBegin(String date) {
        if (date.length() <= 10) {
            date = date + " 00:00:00";
        }
        return LocalDateTime.parse(date, DateTimeFormatters.STANDARD_DATE_TIME_FORMATTER);
    }

    /**
     * 字符串转成localDateTime
     *
     * @author loki
     * @date 2020/04/13 10:22
     */
    public static LocalDateTime str2LocalDateTimeEnd(String date) {
        if (date.length() <= 10) {
            date = date + " 23:59:59";
        }
        return LocalDateTime.parse(date, DateTimeFormatters.STANDARD_DATE_TIME_FORMATTER);
    }

    /**
     * localdatetime 转 str
     *
     * @author loki
     * @date 2021/7/13 15:08
     **/
    public static String localDateTime2Str(LocalDateTime localDateTime) {
        return localDatetime2Str(localDateTime, DateTimeFormatters.STANDARD_DATE_TIME_FORMATTER);
    }

    /**
     * localdatetime 转 str
     *
     * @author loki
     * @date 2021/7/13 15:11
     **/
    public static String localDatetime2Str(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return formatter.format(localDateTime);
    }
}
