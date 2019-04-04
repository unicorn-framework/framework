package org.unicorn.framework.cache.cache.redis;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

/**
 * redis缓存管理
 *
 * @author xiebin
 */
public class UnicornRedisCacheManager extends RedisCacheManager {
    public UnicornRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    @Override
    protected RedisCache createCache(String cacheName) {
        Assert.hasText(cacheName, "CacheName must not be null or empty!");

        String[] values = cacheName.split("#");

        long expiration = computeExpiration(values);
        return new RedisCache(values[0], (isUsePrefix() ? getCachePrefix().prefix(cacheName) : null), getRedisOperations(), expiration,
                false);
    }

    private long computeExpiration(String[] values) {
        if (values.length > 1) {
            return Long.parseLong(values[1]);
        }
        // 如果说想使用默认的过期时间而不指定特殊时间，则可以直接@Cacheable(cacheNames="name")，不需要加'#过期时间'了
        return super.computeExpiration(values[0]);
    }
}
