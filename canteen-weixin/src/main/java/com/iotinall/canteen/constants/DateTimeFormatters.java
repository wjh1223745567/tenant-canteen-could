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
     * yyyy/MM/dd
     */
    DateTimeFormatter STANDARD_DATE_FORMATTER_ = DateTimeFormatter.ofPattern("yyyy/MM/dd");

}
