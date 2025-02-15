package com.ww.user.base.interfaces.config;

import com.ww.common.architecture.log.AccessLogStrategySelector;
import com.ww.user.base.api.service.DemoApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class LogConfig {
    @Bean
    public AccessLogStrategySelector accessLogStrategySelector() {
        return new AccessLogStrategySelector(new HashSet<>(Arrays.asList(
                DemoApi.class)));
    }
}
