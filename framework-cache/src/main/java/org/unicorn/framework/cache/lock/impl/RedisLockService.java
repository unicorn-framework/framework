package org.unicorn.framework.cache.lock.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.lock.LockService;
import org.unicorn.framework.core.exception.PendingException;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author xiebin
 */
@Component("lockService")
@Slf4j
@Primary
public class RedisLockService implements LockService {

    /**
     * 锁命名空间
     */
    private static final String NAMESPACE = "unicorn-lock:";

    /**
     * 尝试获取锁的超时时间 单位毫秒
     */
    @Value("${unicorn.lock.tryTimeout:300}")
    private int tryTimeout = 300;
    /**
     * 获取锁后最长锁定时间 单位秒
     */
    @Value("${unicorn.lock.lockTime:60}")
    private int lockTime = 60;
    /**
     * 实例编号
     */
    @Value("${instance.id:locks}")
    private String instanceId;
    /**
     * 尝试获取锁轮询时间 单位毫秒
     */
    @Value("${unicorn.lock.sleepTime:50}")
    private int sleepTime = 50;

    @Qualifier("stringRedisTemplate")
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    /**
     * 尝试获取锁
     *
     * @param name
     * @return
     */
    @Override
    public boolean tryLock(String name) throws PendingException {
        return tryLock(name, this.tryTimeout, TimeUnit.MILLISECONDS, this.lockTime, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁 可设置尝试时间
     *
     * @param name
     * @param tryTimeout
     * @param tryTimeoutUnit
     * @return
     */
    @Override
    public boolean tryLock(String name, int tryTimeout, TimeUnit tryTimeoutUnit) throws PendingException {
        return tryLock(name, tryTimeout, tryTimeoutUnit, this.lockTime, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁 可设置尝试时间、锁定时间
     *
     * @param name
     * @param tryTimeout
     * @param tryTimeoutUnit
     * @param lockTimeout
     * @param lockTimeoutUnit
     * @return
     */
    @Override
    public boolean tryLock(String name, int tryTimeout, TimeUnit tryTimeoutUnit, int lockTimeout, TimeUnit lockTimeoutUnit) throws PendingException {
        String key = NAMESPACE + name;
        String value = instanceId;
        boolean success = setNxEx(key, value, lockTimeout, lockTimeoutUnit);
        long millisTimeout = tryTimeoutUnit.toMillis(tryTimeout);
        long start = System.currentTimeMillis();
        while (!success) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                return false;
            }
            if ((System.currentTimeMillis() - start) >= millisTimeout) {
                break;
            }
            success = setNxEx(key, value, lockTimeout, lockTimeoutUnit);
        }
        return success;
    }


    @Override
    public void lock(String name) {
        lock(name, this.lockTime, TimeUnit.SECONDS);
    }


    @Override
    public void lock(String name, int lockTime, TimeUnit lockTimeUnit) {
        String key = NAMESPACE + name;
        String value = instanceId;
        boolean success = setNxEx(key, value, lockTime, lockTimeUnit);
        while (!success) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            success = setNxEx(key, value, lockTime, lockTimeUnit);
        }

    }

    /**
     * reids原子化操作
     *
     * @param key
     * @param value
     * @param lockTime
     * @param lockTimeUnit
     * @return
     */
    private boolean setNxEx(String key, String value, int lockTime, TimeUnit lockTimeUnit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, lockTime, lockTimeUnit);
    }

    @Override
    public void unlock(String name) {
        String key = NAMESPACE + name;
        String value = instanceId;
        String str = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";
        RedisScript<Long> script = new DefaultRedisScript<Long>(str, Long.class);
        Long ret = stringRedisTemplate.execute(script, Collections.singletonList(key), value);
        if (ret == 0) {
            log.warn("Lock [{}] is not hold by instance [{}]", name, value);
        }
    }

}
