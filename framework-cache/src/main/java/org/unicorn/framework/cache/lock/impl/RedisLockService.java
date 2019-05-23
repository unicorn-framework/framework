package org.unicorn.framework.cache.lock.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Primary
public class RedisLockService implements LockService {


    private static final String NAMESPACE = "unicorn-lock:";
    private static final Logger log = LoggerFactory.getLogger(RedisLockService.class);

    private RedisTemplate<String, String> stringRedisTemplate;
    /**
     * 尝试获取锁的超时时间 单位毫秒
     */
    @Value("${unicorn.lock.tryTimeout:300}")
    private Long tryTimeout = 300L;
    /**
     * 获取锁后锁定时间 单位秒
     */
    @Value("${unicorn.lock.lockTime:60}")
    private Long lockTime = 60L;

    @Value("${instance.id:locks}")
    private String instanceId;

    @Autowired
    public RedisLockService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

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
    public boolean tryLock(String name, Long tryTimeout, TimeUnit tryTimeoutUnit) throws PendingException {
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
    public boolean tryLock(String name, Long tryTimeout, TimeUnit tryTimeoutUnit, Long lockTimeout, TimeUnit lockTimeoutUnit) throws PendingException {
        String key = NAMESPACE + name;
        String value = instanceId;
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        long millisTimeout = tryTimeoutUnit.toMillis(tryTimeout);
        long start = System.currentTimeMillis();
        while (!success) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
            }
            if ((System.currentTimeMillis() - start) >= millisTimeout) {
                break;
            }
            success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        }
        stringRedisTemplate.expire(key, lockTimeout, lockTimeoutUnit);
        return success;
    }



    @Override
    public void lock(String name) {
        lock(name, this.lockTime, TimeUnit.SECONDS);
    }


    @Override
    public void lock(String name, Long lockTime, TimeUnit lockTimeUnit) {
        String key = NAMESPACE + name;
        String value = instanceId;
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        while (!success) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
            if (success) {
                stringRedisTemplate.expire(key, lockTime, lockTimeUnit);
            }
        }

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
