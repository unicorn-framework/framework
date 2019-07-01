package org.unicorn.framework.cache.cache.redis;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.*;

/**
 * @author  xiebin
 */
public class UnicornRedisCacheManager extends RedisCacheManager implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    private RedisCacheConfiguration defaultCacheConfig;
    Map<String, RedisCacheConfiguration> cacheConfigurationMap = Maps.newHashMap();

    public UnicornRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    @Override
    protected Collection<RedisCache> loadCaches() {
        List<RedisCache> caches = new LinkedList<>();
        Map<String, Long> cacheDurationMap = parseCacheDuration();
        cacheDurationMap.forEach((key, value) -> {
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(defaultCacheConfig.getTtl()).serializeValuesWith(defaultCacheConfig.getValueSerializationPair());
            if (null != value) {
                redisCacheConfiguration.entryTtl(Duration.ofSeconds(value));
            }
            cacheConfigurationMap.put(key, redisCacheConfiguration);
        });
        for (Map.Entry<String, RedisCacheConfiguration> entry : cacheConfigurationMap.entrySet()) {
            caches.add(createRedisCache(entry.getKey(), entry.getValue()));
        }
        return caches;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<String, Long> parseCacheDuration() {
        final Map<String, Long> cacheExpires = new HashMap<>();
        //获取spring容器中所有的bean
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            final Class clazz = applicationContext.getBean(beanName).getClass();

//            Repository service = AnnotationUtils.findAnnotation(obj.getClass(), Repository.class);
//            if (null == service) {
//                continue;
//            }
            addCacheExpires(clazz, cacheExpires);
        }
        return cacheExpires;
    }

    private void addCacheExpires(final Class clazz, final Map<String, Long> cacheExpires) {
        ReflectionUtils.doWithMethods(clazz, method -> {
            ReflectionUtils.makeAccessible(method);
            //获取方法上的Cacheable注解
            Cacheable cacheable = AnnotationUtils.findAnnotation(method, Cacheable.class);
            if(cacheable==null){
                return ;
            }
            //换取缓存名称
            Set<String> cacheNames = Sets.newHashSet(cacheable.cacheNames());
            for (String cacheName : cacheNames) {
                Long expriesTime=null;
                if(cacheName.contains("#")){
                    String cacheNameArr[] = cacheName.split("#");
                    cacheName=cacheNameArr[0];
                    expriesTime=Long.valueOf(cacheNameArr[1]);
                }
                cacheExpires.put(cacheName,expriesTime );
            }
        }, method -> null != AnnotationUtils.findAnnotation(method, Cacheable.class));
    }
}