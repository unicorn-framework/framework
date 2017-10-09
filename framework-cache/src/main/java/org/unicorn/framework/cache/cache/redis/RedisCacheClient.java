package org.unicorn.framework.cache.cache.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.cache.CacheClient;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component("redisCacheClient")
public class RedisCacheClient   implements CacheClient {
	
	private RedisTemplate redisTemplate;

	private RedisTemplate stringRedisTemplate;
	
	//以秒为单位，
	@Value("${cache.timeout:86400}")
    private int timeOut = 86400;

	@Autowired
	public RedisCacheClient(@Qualifier("redisTemplate") RedisTemplate redisTemplate,
			@Qualifier("stringRedisTemplate") RedisTemplate stringRedisTemplate) {
		this.redisTemplate = redisTemplate;
		this.stringRedisTemplate = stringRedisTemplate;
	}

	@Override
	public void set(String key, Object obj) throws PendingException {
		 set(key,obj,timeOut);
	}

	@Override
	public void set(String key, Object value, int timeout) throws PendingException {
		if (key == null || value == null)
			return;
		try {
			redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new PendingException(SysCode.CACHE_CONNECT_FAIL.getCode(), "set Value(key="+key+") to Cache has an Exception",e);
		}
	}

	@Override
	public Object get(String key) throws PendingException {
		return get(key,this.timeOut);
	}

	@Override
	public Object get(String key, int timeout) throws PendingException {
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (SerializationFailedException e) {
//			warn(e.getMessage(), e);
			remove(key);
			return null;
		} catch (Exception e) {
//			warn(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void remove(String key) throws PendingException {
		if (StringUtils.isBlank(key))
			return;
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
//			warn(e.getMessage(), e);
		}
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setStringRedisTemplate(RedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

}
