package org.storm.service.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String arg0) throws CacheException {
        return new RedisCache<>(arg0, redisTemplate);
    }
}
