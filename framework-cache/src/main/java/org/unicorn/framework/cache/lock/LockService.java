package org.unicorn.framework.cache.lock;

import org.unicorn.framework.core.exception.PendingException;

import java.util.concurrent.TimeUnit;

/**
 * @author xiebin
 */
public interface LockService {
    /**
     * 尝试获取锁
     *
     * @param name
     * @return
     * @throws PendingException
     */
    boolean tryLock(String name) throws PendingException;

    /**
     * 尝试获取锁
     *
     * @param name
     * @param tryTimeout
     * @param tryTimeoutUnit
     * @return
     * @throws PendingException
     */
    boolean tryLock(String name, int tryTimeout, TimeUnit tryTimeoutUnit) throws PendingException;

    /**
     * 尝试获取锁
     *
     * @param name            锁名称
     * @param tryTimeout      尝试获取锁超时时间
     * @param tryTimeoutUnit  尝试获取锁超时间单位
     * @param lockTimeout     获取锁成功之后锁定时间
     * @param lockTimeoutUnit 获取锁成功之后锁定时间单位
     * @return
     * @throws PendingException
     */
    boolean tryLock(String name, int tryTimeout, TimeUnit tryTimeoutUnit, int lockTimeout, TimeUnit lockTimeoutUnit) throws PendingException;

    /**
     * 一直阻塞直接获取锁
     *
     * @param name
     * @throws PendingException
     */
    void lock(String name) throws PendingException;

    /**
     * 一直阻塞获取锁
     *
     * @param name
     * @param lockTime
     * @param lockTimeUnit
     * @throws PendingException
     */
    void lock(String name, int lockTime, TimeUnit lockTimeUnit) throws PendingException;

    /**
     * 释放锁
     *
     * @param name
     */
    void unlock(String name);


}