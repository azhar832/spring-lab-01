package kz.iitu.springlab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.format.DateTimeFormatter;

@Configuration
public class FormatConfig {

    @Bean
    public DateTimeFormatter isoFormatter() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    }
}