package org.unicorn.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.base.SpringContextHolder;
/**
 * 
 * @author xiebin
 *
 */
@Configuration
public class BeanConfig {
	
	@Bean
    public SpringContextHolder springContextHolder() {
		return new  SpringContextHolder();
    }
}
