package org.unicorn.framework.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@EnableResourceServer
public class UnicornResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new UnicornAuthExceptionEntryPoint()).and()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
//                .antMatchers("/oauth/token").permitAll()
//                .antMatchers("/**.html", "/**.js", "/**.css", "/**.ico", "/**.ttf").permitAll()
//                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new UnicornAuthExceptionEntryPoint());
    }


}
