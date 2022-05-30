package com.iotinall.canteen.common.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xin-bing
 * @date 10/22/2019 18:03
 */
@Configuration
public class SpringConverterConfiguration {

    /**
     * yyyy-MM-dd HH:mm:ss格式的日期
     */
    private final DateTimeFormatter STANDARD_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * yyyy-MM-dd
     */
    private final DateTimeFormatter STANDARD_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * HH:mm:ss
     */
    private final DateTimeFormatter STANDARD_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    /**
     * 时分
     */
    private final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * description: request param convert to LocalDateTime
     * time: 4/20/2019 09:01
     *
     * @return org.springframework.core.convert.converter.Converter<java.lang.String, java.time.LocalDateTime>
     * @author xin-bing
     * @version 1.0
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                // just judging source length for choice witch formatter
                if (source.length() == 10) {
                    LocalDate localDate = LocalDate.parse(source, STANDARD_DATE_FORMATTER);
                    return LocalDateTime.from(localDate.atStartOfDay());
                }
                if (source.length() == 19) {
                    return LocalDateTime.parse(source, STANDARD_DATE_TIME_FORMATTER);
                } else {
                    throw new IllegalArgumentException("unsupported datetime formatter");
                }
            }
        };
    }

    /**
     * description: request param convert to LocalDate
     * time: 4/20/2019 09:11
     *
     * @return org.springframework.core.convert.converter.Converter<java.lang.String, java.time.LocalDate>
     * @author xin-bing
     * @version 1.0
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                if (source.length() == 10) {
                    return LocalDate.parse(source, STANDARD_DATE_FORMATTER);
                }
                if (source.length() == 19) {
                    return LocalDateTime.parse(source, STANDARD_DATE_TIME_FORMATTER).toLocalDate();
                } else {
                    throw new IllegalArgumentException("unsupported datetime formatter");
                }
            }
        };
    }

    /**
     * description: request param convert to LocalTime
     * time: 4/20/2019 09:12
     *
     * @return org.springframework.core.convert.converter.Converter<java.lang.String, java.time.LocalTime>
     * @author xin-bing
     * @version 1.0
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                if (StringUtils.isBlank(source)) {
                    return null;
                }
                if (source.length() == 19) {
                    return LocalTime.parse(source, STANDARD_DATE_TIME_FORMATTER);
                } else if (source.length() == 5) {
                    return LocalTime.parse(source, HH_MM);
                }
                return LocalTime.parse(source, STANDARD_TIME_FORMATTER);
            }
        };
    }
}
