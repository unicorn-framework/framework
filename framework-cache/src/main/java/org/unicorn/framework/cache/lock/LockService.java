package org.unicorn.framework.cache.lock;

import org.unicorn.framework.core.exception.PendingException;

import java.util.concurrent.TimeUnit;

/**
 * @author  xiebin
 */
public interface LockService {
	final String LIMIT_TIPS_INFO="点击太快了，休息一下，请稍后再试";
	/**
	 * 尝试获取锁
	 * @param name
	 * @return
	 * @throws PendingException
	 */
	boolean tryLock(String name) throws PendingException;

	/**
	 * 尝试获取锁
	 * @param name
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws PendingException
	 */
	 boolean tryLock(String name, int timeout, TimeUnit unit)throws PendingException;

	/**
	 * 尝试获取锁
	 * @param name
	 * @param tryTimeout
	 * @param tryTimeoutUnit
	 * @param lockTimeout
	 * @param lockTimeoutUnit
	 * @return
	 * @throws PendingException
	 */
	 boolean tryLock(String name, int tryTimeout, TimeUnit tryTimeoutUnit,int lockTimeout, TimeUnit lockTimeoutUnit)throws PendingException;

	/**
	 * 一直阻塞获取锁
	 * @param name
	 * @throws PendingException
	 */
	 void lock(String name) throws  PendingException;

	/**
	 * 一直阻塞获取锁
	 * @param name
	 * @param lockTime
	 * @param lockTimeUnit
	 * @throws PendingException
	 */
	 void lock(String name,int lockTime ,TimeUnit lockTimeUnit)throws PendingException;

	/**
	 * 释放锁
	 * @param name
	 */
	 void unlock(String name);



}