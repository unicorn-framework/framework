package org.unicorn.framework.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.unicorn.framework.web.base.SpringContextHolder;
import org.unicorn.framework.web.base.UnicornPasswordEncoder;

import java.util.Locale;

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

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.CHINA);
        return sessionLocaleResolver;
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
