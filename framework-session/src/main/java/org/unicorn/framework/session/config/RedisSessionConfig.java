
package org.unicorn.framework.session.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
*
*@author xiebin
*
*/
@Configuration
@EnableRedisHttpSession
@Import(SessionPropertiesConfig.class)
public class RedisSessionConfig {
	
	@Autowired
	private SessionPropertiesConfig sessionPropertiesConfig;
	
	@Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @PostConstruct
    private void afterPropertiesSet() {
        sessionRepository.setDefaultMaxInactiveInterval(sessionPropertiesConfig.getMaxInactiveIntervalInSeconds());
        sessionRepository.setRedisKeyNamespace(sessionPropertiesConfig.getNamespace());
    }
	@Bean
	public static ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}
	@Bean
	public HttpSessionStrategy cookieAndHeadHttpSessionStrategy(){
		
		return getHttpSessionStrategy();
		
		
		
//		CookieAndHeadHttpSessionStrategy strategy=new CookieAndHeadHttpSessionStrategy();
//		//header
//		HeaderHttpSessionStrategy headerStrategy=new HeaderHttpSessionStrategy();
//		headerStrategy.setHeaderName(sessionPropertiesConfig.getHeadName());
//		//cookie
//		CookieHttpSessionStrategy cookieStrategy=new CookieHttpSessionStrategy();
//		DefaultCookieSerializer cookieSerializer=new DefaultCookieSerializer();
////		cookieSerializer.setCookiePath("/");
//	    cookieSerializer.setCookieName(sessionPropertiesConfig.getCookieName());//cookies名称
//	    cookieStrategy.setCookieSerializer(cookieSerializer);
//	    //添加session策略
//	    strategy.addHttpSessionStrategy(headerStrategy);
//	    strategy.addHttpSessionStrategy(cookieStrategy);
//	    return strategy;
	}
	
	/**
	 * 获取session策略 默认 head
	 * @return
	 */
	
	public  HttpSessionStrategy getHttpSessionStrategy(){
		HeaderHttpSessionStrategy headerStrategy=new HeaderHttpSessionStrategy();
		headerStrategy.setHeaderName(sessionPropertiesConfig.getHeadName());
		return headerStrategy;
		
//		UnicornHttpSessionStrategy headerStrategy=new UnicornHttpSessionStrategy();
//		headerStrategy.setHeaderName(sessionPropertiesConfig.getHeadName());
//		return headerStrategy;
	}
}

