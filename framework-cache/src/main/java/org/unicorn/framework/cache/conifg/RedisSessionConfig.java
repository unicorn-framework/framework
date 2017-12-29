
package org.unicorn.framework.cache.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.unicorn.framework.cache.cookies.CustomerCookieSerializer;
import org.unicorn.framework.cache.session.CookieAndHeadHttpSessionStrategy;

/**
*
*@author xiebin
*
*/
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
public class RedisSessionConfig {
	@Bean
	public HttpSessionStrategy cookieAndHeadHttpSessionStrategy(){
		
		CookieAndHeadHttpSessionStrategy strategy=new CookieAndHeadHttpSessionStrategy();
		//header
		HeaderHttpSessionStrategy headerStrategy=new HeaderHttpSessionStrategy();
		headerStrategy.setHeaderName("sid");
		//cookie
		CookieHttpSessionStrategy cookieStrategy=new CookieHttpSessionStrategy();
		CustomerCookieSerializer cookieSerializer=new CustomerCookieSerializer();
		cookieSerializer.setCookiePath("/");
	    cookieSerializer.setCookieName("JBGSESSIONID");//cookies名称
	    cookieStrategy.setCookieSerializer(cookieSerializer);
	    //添加session策略
	    strategy.addHttpSessionStrategy(headerStrategy);
	    strategy.addHttpSessionStrategy(cookieStrategy);
	    return strategy;
	}
	
}

