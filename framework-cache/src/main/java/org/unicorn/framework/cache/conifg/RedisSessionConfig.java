
package org.unicorn.framework.cache.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;
import org.unicorn.framework.cache.session.CookieAndHeadHttpSessionStrategy;

/**
*
*@author xiebin
*
*/
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
public class RedisSessionConfig {
	
//	public HttpSessionStrategy HeaderHttpSessionStrategy(){
//		HeaderHttpSessionStrategy strategy=new HeaderHttpSessionStrategy();
//	    strategy.setHeaderName("gid");
//	    return strategy;
//	}
//	
//	
//	public MultiHttpSessionStrategy CookieHttpSessionStrategy(){
//		CookieHttpSessionStrategy strategy=new CookieHttpSessionStrategy();
//	    DefaultCookieSerializer cookieSerializer=new DefaultCookieSerializer();
//	    cookieSerializer.setCookieName("JBGSESSIONID");//cookies名称
//	    strategy.setCookieSerializer(cookieSerializer);
//	    return strategy;
//	}
	@Bean
	public MultiHttpSessionStrategy cookieAndHeadHttpSessionStrategy(){
		CookieAndHeadHttpSessionStrategy strategy=new CookieAndHeadHttpSessionStrategy();
		
		HeaderHttpSessionStrategy headerStrategy=new HeaderHttpSessionStrategy();
		headerStrategy.setHeaderName("gid");
		
		strategy.addHttpSessionStrategy(headerStrategy);
		
		CookieHttpSessionStrategy cookieStrategy=new CookieHttpSessionStrategy();
	    DefaultCookieSerializer cookieSerializer=new DefaultCookieSerializer();
	    cookieSerializer.setCookieName("JBGSESSIONID");//cookies名称
	    cookieStrategy.setCookieSerializer(cookieSerializer);
	    strategy.addHttpSessionStrategy(cookieStrategy);
	    return strategy;
	}
	
	
	
	
//	
//	/**
//     * 配置过滤器
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean sessionFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(sessionFilter());
//        registration.addUrlPatterns("/*");
//        registration.addInitParameter("sessionId", "gid");
//        registration.setName("sessionFilter");
//        return registration;
//    }
//
//    /**
//     * 创建一个bean
//     * @return
//     */
//    @Bean(name = "sessionFilter")
//    public Filter sessionFilter() {
//        return new RedisSessionFilter();
//    }
	
}

