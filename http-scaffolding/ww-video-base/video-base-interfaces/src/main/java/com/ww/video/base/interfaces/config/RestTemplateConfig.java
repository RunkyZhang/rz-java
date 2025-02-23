package com.ww.video.base.interfaces.config;

import com.ww.video.base.infrastructure.ConfigSource;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class RestTemplateConfig {
    @Resource
    private ConfigSource configSource;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout((int)configSource.getFeignClientReadTimeout());
        requestFactory.setConnectTimeout((int)configSource.getFeignClientConnectTimeout());

        return new RestTemplate(requestFactory);
    }
}
