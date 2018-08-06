//package org.unicorn.framework.shiro.cache;
//
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheException;
//import org.apache.shiro.cache.CacheManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//@Component
//public class RedisShiroCacheManager implements CacheManager {
//	@Autowired
//	private RedisTemplate<String, Object> objectShiroRedisTemplate;
//	@Override
//	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
//		ShiroRedisCache<K,V> shiroRedisCache=new ShiroRedisCache<K, V>( name);
//		shiroRedisCache.setRedisTemplate( objectShiroRedisTemplate);
//		return shiroRedisCache;
//	}
//
//
//
//		
//}
