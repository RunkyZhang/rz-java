package com.ww.common.architecture.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class LocalCacheProcessor {
    @Resource
    private KeyBuilder keyBuilder;
    int maxErrorCount = 3;
    final Map<String, LocalCache> localCaches = new ConcurrentHashMap<>();

    @Around("process() && @annotation(localCacheable)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, LocalCacheable localCacheable) throws Throwable {
        String key = null;
        try {
            key = keyBuilder.build(proceedingJoinPoint, localCacheable);
        } catch (Throwable throwable) {
            log.warn("failed to build cache key", throwable);
        }
        if (null == key) {
            key = SimpleKeyGenerator.generateKey(proceedingJoinPoint.getArgs()).toString();
        }

        LocalCache localCache = localCaches.get(key);
        if (null == localCache) {
            localCache = new LocalCache(key);
            localCache.setProceedingJoinPoint(proceedingJoinPoint);
            // first do not try catch
            localCache.refresh();
            localCaches.put(localCache.getKey(), localCache);
        } else {
            localCache.setProceedingJoinPoint(proceedingJoinPoint);
            if (localCacheable.expireTime() < (System.currentTimeMillis() - localCache.getRefreshTime()) / 1000) {
                synchronized (localCache) {
                    if (localCacheable.expireTime() < (System.currentTimeMillis() - localCache.getRefreshTime()) / 1000) {
                        try {
                            localCache.refresh();
                        } catch (Throwable throwable) {
                            // fuse when greater then max error count
                            if (this.maxErrorCount < localCache.getErrorCount()) {
                                // via set RefreshTime to fuse
                                localCache.fuse();
                            }

                            log.error("failed to refresh cache(" + key + ")", throwable);
                        }
                    }
                }
            }
        }

        return localCache.getValue();
    }

    @Pointcut("@annotation(com.ww.common.architecture.cache.LocalCacheable)")
    public void process() {
    }

    @Before(value = "process()")
    public void before() {
    }

    @After(value = "process()")
    public void after() {
    }
}
