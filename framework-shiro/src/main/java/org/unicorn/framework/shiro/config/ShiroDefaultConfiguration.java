package org.unicorn.framework.shiro.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.unicorn.framework.cache.conifg.RedisConfig;
import org.unicorn.framework.shiro.cache.ShiroRedisCache;
import org.unicorn.framework.shiro.realm.DefaultSimpleRealm;

/**
 * 
 * @author xiebin
 *
 */
@Configuration
@EnableConfigurationProperties({ShiroProperties.class,ShiroCacheProperties.class})
@Import(RedisConfig.class)
@ConditionalOnBean(name={"objectShiroRedisTemplate"})
public class ShiroDefaultConfiguration {
	@Autowired
	private RedisTemplate<String, ?> objectRedisTemplate;
	@Autowired
	private ShiroProperties shiroProperties;
	@Autowired
	private ShiroCacheProperties shiroCacheProperties;
	
	/**
	 * 注册shiro过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	/*@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}*/
    /**
     * 缓存管理
     * @return
     */
	@Bean
	public CacheManager redisShiroCacheManager(){
		//自定义实现 -redis
		return new CacheManager() {
			@Override
			public <K, V> Cache<K, V> getCache(String name) throws CacheException {
				ShiroRedisCache<K,V> shiroRedisCache=new ShiroRedisCache<K, V>( name);
				shiroRedisCache.setRedisTemplate( objectRedisTemplate);
				shiroRedisCache.setTimeOut(shiroCacheProperties.getAuthorizationCacheSeconds());
				return shiroRedisCache;
			}

		};
	}
	
	
	/**
	 * 安全管理
	 * 
	 * @return
	 */
	@Bean(name = "securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setCacheManager(getCacheManager());
		securityManager.setRealm(getAuthorizingRealm());
		return securityManager;
	}
	/**
	 * shiro緩存管理   可继承扩展
	 * @return
	 */
	public CacheManager getCacheManager(){
		return  redisShiroCacheManager();
	}

	/**
	 * 认证授权 默认实现可扩展
	 * 
	 * @return
	 */
	public AuthorizingRealm getAuthorizingRealm() {
		AuthorizingRealm authorizingRealm = new DefaultSimpleRealm();
		authorizingRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return authorizingRealm;
	}

	/**
	 * 匹配算法 默认实现可扩展
	 * 
	 * @return
	 */
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName(shiroProperties.getHashAlgorithmName());// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(shiroProperties.getHashCount());// 散列的次数，比如散列两次，相当于// md5(md5(""));
		return hashedCredentialsMatcher;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = getShiroFilterFactoryBean(securityManager);
		loadShiroFilterChain(shiroFilterFactoryBean);
		return shiroFilterFactoryBean;
	}

	/**
	 * ShiroFilterFactoryBean 默认实现
	 * 
	 * @param securityManager
	 * @return
	 */
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setSuccessUrl(shiroProperties.getSuccessUrl());
		shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties.getUnAuthorizedUrl());
		shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
		return shiroFilterFactoryBean;
	}

	/**
	 * 
	 * @param shiroFilterFactoryBean
	 */
	public void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put(shiroProperties.getLogOutUrl(), "logout");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}

	/**
	 * aop
	 * 
	 * @return
	 */
	@Bean
//	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(getDefaultWebSecurityManager());
		return authorizationAttributeSourceAdvisor;
	}
}
