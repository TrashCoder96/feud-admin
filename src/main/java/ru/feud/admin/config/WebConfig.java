package ru.feud.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    @Bean
    public Filter restLoggingFilter() {
        RestLoggingFilter loggingFilter = new RestLoggingFilter();
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeQueryString(true);
        return loggingFilter;
    }

}
