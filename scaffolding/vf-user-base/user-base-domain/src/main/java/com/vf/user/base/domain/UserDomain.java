package com.vf.user.base.domain;

import com.vf.user.base.api.entity.UserEntity;
import com.vf.user.base.infrastructure.RedisMapper;
import com.vf.user.base.infrastructure.mapper.UserMapper;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class UserDomain {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisMapper redisMapper;

    public UserEntity getByUserId(int userId) {
        RBucket<UserEntity> rBucket = redisMapper.getDefaultRedis().getBucket("ub.ue:" + userId);
        UserEntity userEntity = rBucket.get();
        if (null == userEntity) {
            userEntity = userMapper.getByUserId(userId);
            if (null == userEntity) {
                return null;
            }

            rBucket.set(userEntity);
            rBucket.expire(60, TimeUnit.SECONDS);
        }

        return userEntity;
    }
}
