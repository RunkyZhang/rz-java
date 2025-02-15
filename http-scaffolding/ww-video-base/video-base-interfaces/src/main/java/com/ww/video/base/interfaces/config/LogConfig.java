package com.ww.video.base.interfaces.config;

import com.ww.common.architecture.log.AccessLogStrategySelector;
import com.ww.video.base.api.service.SomeApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class LogConfig {
    @Bean
    public AccessLogStrategySelector accessLogStrategySelector() {
        return new AccessLogStrategySelector(new HashSet<>(Arrays.asList(
                SomeApi.class)));
    }
}
