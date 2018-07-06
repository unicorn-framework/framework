package org.unicorn.framework.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.base.base.SpringContextHolder;
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
