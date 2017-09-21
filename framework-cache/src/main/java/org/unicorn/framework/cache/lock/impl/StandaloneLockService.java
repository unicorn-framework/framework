package org.unicorn.framework.cache.lock.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.lock.LockService;
/**
 * 
 * @author xiebin
 *
 */
@Component("standaloneLockService")
public class StandaloneLockService implements LockService {

	private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<String, Lock>();

	@Override
	public boolean tryLock(String name) {
		return getLock(name).tryLock();
	}

	@Override
	public boolean tryLock(String name, long timeout, TimeUnit unit) {
		Lock lock = getLock(name);
		try {
			return lock.tryLock(timeout, unit);
		} catch (InterruptedException e) {
			return false;
		}
	}

	@Override
	public void lock(String name) {
		getLock(name).lock();
	}

	@Override
	public void unlock(String name) {
		getLock(name).unlock();
	}

	private Lock getLock(String name) {
		Lock lock = locks.get(name);
		if (lock == null) {
			Lock newLock = new ReentrantLock();
			lock = locks.putIfAbsent(name, newLock);
			if (lock == null)
				lock = newLock;
		}
		return lock;
	}
}
