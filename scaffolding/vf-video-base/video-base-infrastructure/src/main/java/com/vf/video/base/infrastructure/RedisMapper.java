package com.vf.video.base.infrastructure;

import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisMapper {
    @Resource
    private RedissonClient redissonClient;

    public RedissonClient getDefaultRedis() {
        return redissonClient;
    }
}
