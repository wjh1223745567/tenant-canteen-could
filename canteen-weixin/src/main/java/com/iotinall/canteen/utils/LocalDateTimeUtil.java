package com.iotinall.canteen.utils;

import com.iotinall.canteen.constants.DateTimeFormatters;

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
}
