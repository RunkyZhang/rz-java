package com.ww.user.base.infrastructure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service
@Getter
@RefreshScope
public class ConfigSource {
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;
    @Value("${userName:1111}")
    private String userName;
    @Value("${serverAddress:11。11。11。11}")
    private String serverAddress;
    @Value("${feign.client.config.default.connect-timeout:5000}")
    private long feignClientConnectTimeout;
}
