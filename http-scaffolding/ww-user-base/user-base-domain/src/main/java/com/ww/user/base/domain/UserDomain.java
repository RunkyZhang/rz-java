package com.ww.user.base.domain;

import com.ww.common.architecture.cache.LocalCacheable;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.infrastructure.RedisMapper;
import com.ww.user.base.infrastructure.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
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
        log.debug("log-test-debug--UserDomain--hohouhou");
        log.info("log-test-info--UserDomain--hohouhou");
        log.warn("log-test-warn--UserDomain--hohouhou");
        log.error("log-test-error--UserDomain--hohouhou");

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

    public Map<Long, UserEntity> getByUserIds(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return null;
        }

        // 用户表分片为32个分片，根据userId取模得到分片组号
        Map<Long, Set<Long>> shardingGroups = new HashMap<>();
        for (Long userId : userIds) {
            long key = userId % 32;
            shardingGroups.computeIfAbsent(key, o -> new HashSet<>()).add(userId);
        }

        // TODO：sharding jdbc批量查询有问题，暂时先循环查询
        // 举例：查询100000000, 100000047, 100000015分别在db0.ww_user0, db1.ww_user15分片中。最终shārding jdbc查询结果为以下两行
        // 举例：INFO |ShardingSphere-SQL:Actual SQL: db0 ::: select *  from ww_user0  where deleted != 0  and id in   (      ?   ,     ?   ,     ?   ) UNION ALL select *  from ww_user15  where deleted != 0  and id in   (      ?   ,     ?   ,     ?   ) ::: [100000000, 100000047, 100000015, 100000000, 100000047, 100000015]
        // 举例：INFO |ShardingSphere-SQL:Actual SQL: db1 ::: select *  from ww_user0  where deleted != 0  and id in   (      ?   ,     ?   ,     ?   ) UNION ALL select *  from ww_user15  where deleted != 0  and id in   (      ?   ,     ?   ,     ?   ) ::: [100000000, 100000047, 100000015, 100000000, 100000047, 100000015]
        // 举例：最终会到两个库（db0，和db1）中都去查询一下。但db0没有ww_user15表db1没有ww_user0表。所以会报错
        Map<Long, UserEntity> userEntities = new HashMap<>();
        for (Set<Long> subUserIds : shardingGroups.values()) {
            if (CollectionUtils.isEmpty(subUserIds)) {
                continue;
            }
            Map<Long, UserEntity> subUserEntities = userMapper.selectByIds(subUserIds);
            if (CollectionUtils.isEmpty(subUserEntities)) {
                continue;
            }
            userEntities.putAll(subUserEntities);
        }

        return userEntities;
    }

//    public Map<Long, UserEntity> getByUserIds(Set<Long> userIds) {
//        return userMapper.selectByIds(userIds);
//    }
}
