package com.ww.user.base.interfaces.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                //可以用"rediss://"来启用SSL连接，前缀必须是redis:// or rediss://
                //.setPassword("dTxSX5tZeMuHiG8v")
                .setAddress("redis://localhost:6379");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonJacksonCodec jsonJacksonCodec = new JsonJacksonCodec(objectMapper);
        config.setCodec(jsonJacksonCodec);
        return Redisson.create(config);
    }
}
