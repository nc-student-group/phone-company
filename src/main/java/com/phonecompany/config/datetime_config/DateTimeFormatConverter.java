package com.phonecompany.config.datetime_config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeFormatConverter implements Converter<String, LocalDate> {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDate convert(String source) {
        return LocalDate.parse(source, FORMATTER);
    }
}
