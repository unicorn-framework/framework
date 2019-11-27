package org.unicorn.framework.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.cache.lock.LockService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

import java.util.concurrent.TimeUnit;

/**
 * 缓存操作模板：缓存防穿透和雪崩
 *
 * @author xiebin
 */
@Component
public class UnicornCacheOperationTemplate {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private LockService lockService;
    /**
     * 初始化 128个锁，用于应对缓存击穿,并发处理
     */
    public static final String[] LOCKS = new String[128];

    /**
     * 在静态块中将128个锁先初始化出来
     */
    static {
        for (int i = 0; i < 128; i++) {
            LOCKS[i] = "lock_" + i;
        }
    }

    /**
     * 缓存防穿透和雪崩
     *
     * @param cacheKey
     * @param namespace
     * @param unicornCacheCallback
     * @param <T>
     * @return
     * @throws PendingException
     */
    public <T> T doExecute(String cacheKey, String namespace, UnicornCacheCallback<T> unicornCacheCallback) throws PendingException {
        return doExecute(cacheKey, namespace, null, null, unicornCacheCallback);
    }


    /**
     * 缓存防穿透和雪崩
     *
     * @param cacheKey
     * @param namespace
     * @param timeout
     * @param timeUnit
     * @param unicornCacheCallback
     * @param <T>
     * @return
     * @throws PendingException
     */
    public <T> T doExecute(String cacheKey, String namespace, Integer timeout, TimeUnit timeUnit, UnicornCacheCallback<T> unicornCacheCallback) throws PendingException {
        //判断缓存是否存在
        if (cacheService.exists(cacheKey, namespace)) {
            //存在则直接返回
            return (T) cacheService.get(cacheKey, namespace);
        } else {
            //不存在则需要先获取到锁
            String lockKey = genLockKey(cacheKey, namespace);
            try {
                //尝试获取锁
                lockService.tryLock(lockKey);
                if (cacheService.exists(cacheKey, namespace)) {
                    //存在则直接返回
                    return (T) cacheService.get(cacheKey, namespace);
                }
                //调用DB查询
                T t = unicornCacheCallback.doInCache();
                //存入到缓存
                if (timeout != null && timeUnit != null) {
                    cacheService.put(cacheKey, t, timeout, timeUnit, namespace);
                }

                return t;
            } catch (PendingException pe) {
                throw pe;
            } catch (Exception e) {
                throw new PendingException(SysCode.SYS_FAIL, e);
            } finally {
                lockService.unlock(lockKey);
            }
        }
    }


    /**
     * 生成锁
     *
     * @param cacheKey
     * @param namespace
     * @return
     */
    public String genLockKey(String cacheKey, String namespace) {
        int index = (namespace + ":" + cacheKey + ":" + System.currentTimeMillis()).hashCode() & (LOCKS.length - 1);
        return LOCKS[index];
    }
}
