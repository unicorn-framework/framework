package org.unicorn.framework.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.unicorn.framework.oauth.filter.PermitAuthenticationFilter;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@EnableResourceServer
@EnableConfigurationProperties(OAuth2Properties.class)
public class UnicornResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private OAuth2Properties oAuth2Properties;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        PermitAuthenticationFilter permitAuthenticationFilter=new PermitAuthenticationFilter(oAuth2Properties);
        http.addFilterBefore(permitAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
        http.exceptionHandling().authenticationEntryPoint(new UnicornAuthExceptionEntryPoint()).and()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/v2/**", "/swagger**").permitAll()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/**.html", "/**.js", "/**.css", "/**.ico", "/**.ttf").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.authenticationEntryPoint(new UnicornAuthExceptionEntryPoint());
    }


}
