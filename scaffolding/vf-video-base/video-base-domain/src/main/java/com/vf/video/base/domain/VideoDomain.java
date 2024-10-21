package com.vf.video.base.domain;

import com.vf.common.architecture.cache.LocalCacheable;
import com.vf.video.base.api.entity.VideoEntity;
import com.vf.video.base.infrastructure.RedisMapper;
import com.vf.video.base.infrastructure.mapper.VideoMapper;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class VideoDomain {
    public static final String cacheKey = "VideoDomain_cache_key";

    @Resource
    private VideoMapper videoMapper;
    @Resource
    private RedisMapper redisMapper;

    @LocalCacheable(
            expireTime = 30,
            keyExpression = "T(com.vf.video.base.domain.VideoDomain).cacheKey")
    public VideoEntity getByVideoId(long videoId) {
        RBucket<VideoEntity> rBucket = redisMapper.getDefaultRedis().getBucket("vb.ve:" + videoId);
        VideoEntity videoEntity = rBucket.get();
        if (null == videoEntity) {
            videoEntity = videoMapper.getByVideoId(videoId);
            if (null == videoEntity) {
                return null;
            }

            rBucket.set(videoEntity);
            rBucket.expire(60, TimeUnit.SECONDS);
        }

        return videoEntity;
    }
}
