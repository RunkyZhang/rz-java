package com.ww.video.base.interfaces.config;

import com.ww.common.base.JacksonHelper;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Value("${redis.config.address:}")
    private String address;
    @Value("${redis.config.password:}")
    private String password;
    @Value("${redis.config.database:0}")
    private int database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                //可以用"rediss://"来启用SSL连接，前缀必须是redis:// or rediss://
                .setAddress(address)
                .setDatabase(database);
        if (!StringUtils.isBlank(password)) {
            config.useSingleServer().setPassword(password);
        }

        JsonJacksonCodec jsonJacksonCodec = new JsonJacksonCodec(JacksonHelper.mapper);
        config.setCodec(jsonJacksonCodec);
        return Redisson.create(config);
    }
}
