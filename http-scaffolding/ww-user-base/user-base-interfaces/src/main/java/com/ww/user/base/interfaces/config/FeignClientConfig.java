package com.ww.user.base.interfaces.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    // 全局默认重试设置
    @Bean
    protected Retryer retryer() {
        // 重试间隔为100～1000毫秒，最多调用2次（正常1次+重试1次）
        // 第一次100 * 1.5; 第二次100 * 1.5 * 1.5; 第三次100 * 1.5 * 1.5 * 1.5。等等但是不超过1000毫秒
        return new Retryer.Default(100, 1000, 2);
    }
}
