package com.iotinall.canteen.common.configuration;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class FeignDateFormatRegister implements FeignFormatterRegistrar {

    @Override
    public void registerFormatters(FormatterRegistry registry) {
        registry.addConverter(LocalDate.class, String.class, new Date2StringConverter());
    }

    private static class Date2StringConverter implements Converter<LocalDate,String> {

        @Override
        public String convert(LocalDate source) {
            return source.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

    }

}
