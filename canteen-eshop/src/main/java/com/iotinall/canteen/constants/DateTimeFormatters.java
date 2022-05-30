package com.iotinall.canteen.constants;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式
 *
 * @author xin-bing
 * @date 10/17/2019 21:34
 */
public interface DateTimeFormatters {
    /**
     * yyyy-MM-dd HH:mm:ss格式的日期
     */
    DateTimeFormatter STANDARD_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyy-MM-dd
     */
    DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * yyyy-MM-dd HH:mm
     */
    DateTimeFormatter yyyyMMddHHmm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * yyyy-MM-dd
     */
    DateTimeFormatter STANDARD_DATE_YYYY_MM = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * yyyy-MM-dd
     */
    DateTimeFormatter STANDARD_DATE_FORMATTER_CH = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    /**
     * yyyy/MM/dd
     */
    DateTimeFormatter STANDARD_DATE_FORMATTER_ = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * HH:mm:ss
     */
    DateTimeFormatter STANDARD_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * yyyyMMddHHmmssSSS
     */
    DateTimeFormatter YYYYMMDDHHMM5S = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * yyyyMMddHHmmss
     */
    DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * yyyyMMddHHmmssSSS
     */
    DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 月日
     */
    DateTimeFormatter MMDD = DateTimeFormatter.ofPattern("MMdd");

    /**
     * 时分
     */
    DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 时分
     */
    DateTimeFormatter MM_SS = DateTimeFormatter.ofPattern("mmss");
}
