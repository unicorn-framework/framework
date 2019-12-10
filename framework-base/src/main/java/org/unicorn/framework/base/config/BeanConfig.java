package org.unicorn.framework.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.base.base.UnicornPasswordEncoder;

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
        return new UnicornPasswordEncoder();
    }
    public static void main(String[] args) {
        UnicornPasswordEncoder unicornPasswordEncoder=new UnicornPasswordEncoder();
        Long start =System.currentTimeMillis();
        System.out.println(unicornPasswordEncoder.encode("123456"));
        Long end =System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(unicornPasswordEncoder.matches("123456",unicornPasswordEncoder.encode("123456")));
    }
}
