package com.vf.video.base.domain;

import com.vf.video.base.api.entity.UserEntity;
import com.vf.video.base.infrastructure.RedisMapper;
import com.vf.video.base.infrastructure.mapper.UserMapper;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
            if(null == userEntity) {
                return null;
            }

            redisMapper.getDefaultRedis().getBucket("ub.ue:" + userId).set(userEntity);
        }

        return userEntity;
    }
}
