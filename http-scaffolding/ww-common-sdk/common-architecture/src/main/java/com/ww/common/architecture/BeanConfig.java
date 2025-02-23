package com.ww.common.architecture;

import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RefreshScope
public class BeanConfig {
    @Value("${feign.client.config.default.connect-timeout:5000}")
    private int feignClientConnectTimeout;
    @Value("${feign.client.config.default.read-timeout:1000}")
    private int feignClientReadTimeout;

    // 全局默认重试设置
    @Bean
    @ConditionalOnMissingBean
    protected Retryer retryer() {
        // 重试间隔为100～1000毫秒，最多调用2次（正常1次+重试1次）
        // 第一次100 * 1.5; 第二次100 * 1.5 * 1.5; 第三次100 * 1.5 * 1.5 * 1.5。等等但是不超过1000毫秒
        return new Retryer.Default(100, 1000, 2);
    }

    @LoadBalanced
    @ConditionalOnMissingBean
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(feignClientReadTimeout);
        requestFactory.setReadTimeout(feignClientConnectTimeout);

        return new RestTemplate(requestFactory);
    }
}
