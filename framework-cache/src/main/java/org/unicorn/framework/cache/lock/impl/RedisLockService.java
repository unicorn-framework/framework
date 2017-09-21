package org.unicorn.framework.cache.lock.impl;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

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
/**
 * 
 * @author xiebin
 *
 */
@Component("lockService")
@Primary
public class RedisLockService implements LockService {

    private static final String NAMESPACE = "unicorn-lock:";
    private static final Logger log = LoggerFactory.getLogger(RedisLockService.class);

    private RedisTemplate<String, String> stringRedisTemplate;

    @Value("${lockService.timeout:300}")
    private int timeout = 300;

    @Value("${instance.id:locks}")
    private String instanceId;

    @Autowired
    public RedisLockService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(String name) {
        String key = NAMESPACE + name;
        String value = instanceId;
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        if (success)
            stringRedisTemplate.expire(key, this.timeout, TimeUnit.SECONDS);
        return success;
    }

    @Override
    public boolean tryLock(String name, long timeout, TimeUnit unit) {
        if (timeout <= 0)
            return tryLock(name);
        String key = NAMESPACE + name;
        String value = instanceId;
        boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        long millisTimeout = unit.toMillis(timeout);
        long start = System.currentTimeMillis();
        while (!success) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
            }
            if ((System.currentTimeMillis() - start) >= millisTimeout)
                break;
            success = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        }
        if (success)
            stringRedisTemplate.expire(key, this.timeout, TimeUnit.SECONDS);
        return success;
    }

    @Override
    public void lock(String name) {
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
            if (success)
                stringRedisTemplate.expire(key, this.timeout, TimeUnit.SECONDS);
        }

    }

    @Override
    public void unlock(String name) {
        String key = NAMESPACE + name;
        String value = instanceId;
        String str = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";
        RedisScript<Long> script = new DefaultRedisScript<Long>(str, Long.class);
        Long ret = stringRedisTemplate.execute(script, Collections.singletonList(key), value);
        if (ret == 0)
            log.warn("Lock [{}] is not hold by instance [{}]", name, value);
    }

}
