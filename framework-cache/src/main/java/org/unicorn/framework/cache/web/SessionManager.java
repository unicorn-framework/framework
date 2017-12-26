/**
 * Title: SessionManager.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.cache.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

/**
 * Title: SessionManager<br/>
 * Description: <br/>
 * 
 *
 * @author xiebin
 *
 */

@Component
@SuppressWarnings("rawtypes")
public class SessionManager {

	// private static SessionManager sessionManager = new SessionManager();

	public static final String CACHE_NAMESPACE = "session";

	@Autowired
	private CacheService cacheService;

	// session过期时间 单位为秒 -->
	@Value("${session.timeout:1800}")
	private int sessionTimeOut = 0;

	private void add(String key, int timeOut, Map value) throws PendingException {
		cacheService.put(key, value, timeOut, TimeUnit.SECONDS, CACHE_NAMESPACE);
	}

	private void update(String key, int timeOut, Map value) throws PendingException {
		cacheService.put(key, value, timeOut, TimeUnit.SECONDS, CACHE_NAMESPACE);
	}

	// todo 有超时的异常
	private Map get(String key, int timeOut) throws PendingException {
		Object value = cacheService.get(key, CACHE_NAMESPACE, timeOut, TimeUnit.SECONDS);
		if(value != null) {
			return (Map) value;
		}
		
		return null;
	}

	private void delete(String key) throws InterruptedException, ExecutionException {
		cacheService.delete(key, CACHE_NAMESPACE);
	}

	public void setSession(String sid, Map session) throws PendingException {
		update(sid, this.sessionTimeOut, session);
	}

	public Map getSession(String sid) throws PendingException {
		Map session = null;
		try {
			session = get(sid, this.sessionTimeOut);
			if (null == session) {
				session = new HashMap();
				add(sid, this.sessionTimeOut, session);
			}
		} catch (Exception e) {
			throw new PendingException(SysCode.CACHE_CONNECT_FAIL, e.getMessage());
		}
		return session;
	}

	public void removeSession(String sid) throws PendingException {
		try {
			delete(sid);
		} catch (ExecutionException | InterruptedException e) {
			throw new PendingException(SysCode.CACHE_CONNECT_FAIL, e.getMessage());
		}

	}
	
	public void initialize(String sid) throws PendingException {
		get(sid, this.sessionTimeOut);
	}

	/**
	 * @param sessionTimeOut the sessionTimeOut to set
	 */
	public void setSessionTimeOut(int sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

}
