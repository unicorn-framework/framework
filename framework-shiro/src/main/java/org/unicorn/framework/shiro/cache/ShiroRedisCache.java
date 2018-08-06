package org.unicorn.framework.shiro.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * 
 * @author xiebin
 *
 * @param <K>
 * @param <V>
 */
public class ShiroRedisCache<K, V> implements Cache<K, V> {
	//shoir 权限缓存前缀
	private String cacheKeyPrefix = "unicorn:shiro:authorization:";
	private String cacheKey;
	private Long timeOut;
	private RedisTemplate<String, ?> redisTemplate;

	public RedisTemplate<String, ?> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, ?> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public ShiroRedisCache(String cacheKey) {
		this.cacheKey = cacheKeyPrefix+cacheKey;
	}

	@Override
	public V get(K key) throws CacheException {
		BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
		Object k = hashKey(key);
		return hash.get(k);
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) throws CacheException {
		BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
		Object k = hashKey(key);
		hash.expire(timeOut, TimeUnit.SECONDS);
		hash.put((K) k, value);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
		BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
		Object k = hashKey(key);
		V value = hash.get(k);
		hash.delete(k);
		return value;
	}

	@Override
	public void clear() throws CacheException {
		redisTemplate.delete(cacheKey);
	}

	@Override
	public int size() {
		BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
		return hash.size().intValue();
	}

	@Override
	public Set<K> keys() {
		BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
		return hash.keys();
	}

	@Override
	public Collection<V> values() {
		BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
		return hash.values();
	}

	protected Object hashKey(K key) {
		
		return String.valueOf(key);
	}

	public Long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}
	
	
}
