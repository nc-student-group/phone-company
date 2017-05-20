package com.phonecompany.config.datetime_config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

import static com.phonecompany.config.datetime_config.DateTimeFormatConverter.FORMATTER;

@Component
public class LocalDateTimeSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeString(value.format(FORMATTER));
    }
}
