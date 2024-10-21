package com.vf.user.base.interfaces.config;

import com.vf.common.architecture.log.AccessLogStrategySelector;
import com.vf.user.base.api.service.DemoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class LogConfig {
    @Bean
    public AccessLogStrategySelector accessLogStrategySelector() {
        return new AccessLogStrategySelector(new HashSet<>(Arrays.asList(
                DemoService.class)));
    }
}
