package com.ww.user.base.domain;

import com.ww.common.architecture.cache.LocalCacheable;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.infrastructure.RedisMapper;
import com.ww.user.base.infrastructure.mapper.UserMapper;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserDomain {
    public static final String cacheKey = "UserDomain_cache_key";

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisMapper redisMapper;

    @LocalCacheable(
            expireTime = 30,
            keyExpression = "T(com.ww.user.base.domain.UserDomain).cacheKey")
    public UserEntity getByUserId(long userId) {
        RBucket<UserEntity> rBucket = redisMapper.getDefaultRedis().getBucket("ub.ue:" + userId);
        UserEntity userEntity = rBucket.get();
        if (null == userEntity) {
            userEntity = userMapper.selectById(userId);
            if (null == userEntity) {
                return null;
            }

            rBucket.set(userEntity);
            rBucket.expire(60, TimeUnit.SECONDS);
        }

        return userEntity;
    }

    public List<UserEntity> getByPhoneNo(String phoneNo) {
        return userMapper.selectByPhoneNo(phoneNo);
    }

    public Map<Long, UserEntity> getByUserIds(Set<Long> userIds) {
        return userMapper.selectByIds(userIds);
    }
}
