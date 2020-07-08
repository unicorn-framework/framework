package org.unicorn.framework.cache.cache;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xiebin
 */
public interface CacheService {
    /**
     * 设置对象
     *
     * @param key
     * @param value
     * @param timeToLive
     * @param timeUnit
     * @param namespace
     */
    void put(String key, Object value, int timeToLive, TimeUnit timeUnit, String namespace);

    /**
     * 设置对象
     *
     * @param key
     * @param value
     * @param timeToIdle
     * @param timeToLive
     * @param timeUnit
     * @param namespace
     */
    void put(String key, Object value, int timeToIdle, int timeToLive, TimeUnit timeUnit, String namespace);

    /**
     * 是否存在对应的key
     *
     * @param key
     * @param namespace
     * @return
     */
    boolean exists(String key, String namespace);

    /**
     * 获取key对应的值
     *
     * @param key
     * @param namespace
     * @return
     */
    Object get(String key, String namespace);

    /**
     * 获取key对应的值
     *
     * @param key
     * @param namespace
     * @param clazz
     * @return
     */
    <T> T get(String key, String namespace, Class<T> clazz);

    /**
     * 获取key对应的值
     *
     * @param key
     * @param namespace
     * @param timeToIdle
     * @param timeUnit
     * @return
     */
    Object get(String key, String namespace, int timeToIdle, TimeUnit timeUnit);

    /**
     * 获取key对应的值
     *
     * @param key
     * @param namespace
     * @param timeToIdle
     * @param timeUnit
     * @param clazz
     * @return
     */
    <T> T get(String key, String namespace, int timeToIdle, TimeUnit timeUnit, Class<T> clazz);


    /**
     * 删除指定key
     *
     * @param key
     * @param namespace
     */
    void delete(String key, String namespace);

    /**
     * 设置hash对象
     *
     * @param map
     * @param timeToLive
     * @param timeUnit
     * @param namespace
     */
    void mput(Map<String, Object> map, int timeToLive, TimeUnit timeUnit, String namespace);

    /**
     * 获取hash对象
     *
     * @param keys
     * @param namespace
     * @return
     */
    Map<String, Object> mget(Collection<String> keys, String namespace);

    /**
     * 删除hash对象
     *
     * @param keys
     * @param namespace
     */
    void mdelete(Collection<String> keys, String namespace);

    /**
     * 是否包含key
     *
     * @param key
     * @param namespace
     * @return
     */
    boolean containsKey(String key, String namespace);

    /**
     * 设置如果不存在
     *
     * @param key
     * @param value
     * @param timeToLive
     * @param timeUnit
     * @param namespace
     * @return
     */
    boolean putIfAbsent(String key, Object value, int timeToLive, TimeUnit timeUnit, String namespace);

    /**
     * 设置如果不存在
     *
     * @param key
     * @param value
     * @param expireDate
     * @param namespace
     * @return
     */
    boolean putIfAbsent(String key, Object value, Date expireDate, String namespace);

    /**
     * incrby 原子操作
     *
     * @param key
     * @param delta
     * @param timeToLive
     * @param timeUnit
     * @param namespace
     * @return
     */
    long increment(String key, long delta, int timeToLive, TimeUnit timeUnit, String namespace);

    /**
     * incrby 原子操作
     *
     * @param key
     * @param delta
     * @param namespace
     * @return
     */
    long increment(String key, long delta, String namespace);


}
