package org.unicorn.framework.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
