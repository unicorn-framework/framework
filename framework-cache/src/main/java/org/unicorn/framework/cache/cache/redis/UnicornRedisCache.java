package org.unicorn.framework.cache.cache.redis;

import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * @author xiebin
 */
public class UnicornRedisCache extends RedisCache {
    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration cacheConfig;
    protected UnicornRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfig = cacheConfig;

    }

    private byte[] createAndConvertCacheKey(Object key) {
        return this.serializeCacheKey(this.createCacheKey(key));
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        Duration duration=getDuration();
        Object cacheValue = this.preProcessCacheValue(value);
        if (!this.isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format("Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.", this.name));
        } else {
            this.cacheWriter.put(this.name, this.createAndConvertCacheKey(key), this.serializeCacheValue(cacheValue), duration);
        }
    }

    /**
     * 获取
     * @return
     */
    public Duration getDuration(){
        Long expireTime=null;
        Duration duration=this.cacheConfig.getTtl();
        if(this.name.contains("#")){
            expireTime=Long.valueOf(this.name.split("#")[1]);
            duration=Duration.ofSeconds(expireTime);
        }
        return duration;
    }
    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {

        Object cacheValue = this.preProcessCacheValue(value);
        if (!this.isAllowNullValues() && cacheValue == null) {
            return this.get(key);
        } else {
            byte[] result = this.cacheWriter.putIfAbsent(this.name, this.createAndConvertCacheKey(key), this.serializeCacheValue(cacheValue), this.cacheConfig.getTtl());
            return result == null ? null : new SimpleValueWrapper(this.fromStoreValue(this.deserializeCacheValue(result)));
        }
    }



}