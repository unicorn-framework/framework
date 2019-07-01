//
//package org.unicorn.framework.cache.aop;
//
//import com.google.common.collect.Sets;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.stereotype.Component;
//import org.unicorn.framework.base.base.AbstractService;
//import org.unicorn.framework.base.base.SpringContextHolder;
//
//import java.time.Duration;
//import java.util.Map;
//import java.util.Set;
//
///**
// *
// * @author xiebin
// *
// */
//@Aspect
//@Component
//public class CacheableAspect extends AbstractService {
//
//	@Pointcut("@annotation(org.springframework.cache.annotation.Cacheable)")
//	public void cacheableCut() {
//	}
//
//
//
//	@Before("cacheableCut()&&@annotation(cacheable)") //
//	public void before(JoinPoint  pjp, Cacheable cacheable) {
//		RedisCacheManager redisCacheManager=SpringContextHolder.getApplicationContext().getBean(RedisCacheManager.class);
//		//换取缓存名称
//		Set<String> cacheNames = Sets.newHashSet(cacheable.cacheNames());
//		for (String cacheName : cacheNames) {
//			Long expriesTime = null;
//			if (cacheName.contains("#")) {
//				String cacheNameArr[] = cacheName.split("#");
////				cacheName = cacheNameArr[0];
//				expriesTime = Long.valueOf(cacheNameArr[1]);
//			}
//			for(String key:redisCacheManager.getCacheConfigurations().keySet()){
//				RedisCacheConfiguration redisCacheConfiguration = redisCacheManager.getCacheConfigurations().get(key);
//				redisCacheManager.getCacheConfigurations().remove(key);
//				if(cacheName.equals(key)){
//					redisCacheConfiguration.entryTtl(Duration.ofSeconds(expriesTime));
//					redisCacheManager.getCacheConfigurations().put(key,redisCacheConfiguration);
//				}
//			}
//
//		}
//
//	}
//
//}
