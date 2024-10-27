package com.ww.common.architecture.cache;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class LocalCacheManager {
    @Resource
    private LocalCacheProcessor localCacheProcessor;

    public Set<String> getKeys() {
        return this.localCacheProcessor.localCaches.keySet();
    }

    public void expireCache(String key) {
        LocalCache localCache = this.localCacheProcessor.localCaches.get(key);
        if (null == localCache) {
            return;
        }

        localCache.expire();
    }

    public void clearCache(String key) {
        if (this.localCacheProcessor.localCaches.containsKey(key)) {
            this.localCacheProcessor.localCaches.put(key, null);
        }
    }

    public void refreshCache(String key) throws Throwable {
        LocalCache localCache = this.localCacheProcessor.localCaches.get(key);
        if (null == localCache) {
            return;
        }

        localCache.refresh();
    }
}
