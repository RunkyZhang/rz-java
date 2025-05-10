package com.rz.web.demo.chat;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 300000);
    }
}
