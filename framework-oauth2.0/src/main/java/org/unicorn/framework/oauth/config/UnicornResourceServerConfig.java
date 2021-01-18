package org.unicorn.framework.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.unicorn.framework.oauth.filter.UnicornPermitAuthenticationFilter;
import org.unicorn.framework.oauth.handler.UnicornAuthExceptionEntryPoint;
import org.unicorn.framework.oauth.properties.OAuth2Properties;
import org.unicorn.framework.util.json.JsonUtils;

import java.util.List;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@EnableResourceServer
@EnableConfigurationProperties({OAuth2Properties.class})
@Slf4j
public class UnicornResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private OAuth2Properties oauth2Properties;
    @Autowired
    private UnicornPermitAuthenticationFilter unicornPermitAuthenticationFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<String> permitList = oauth2Properties.getPermitAlls();
        permitList.add("/oauth/**");
        permitList.add("/**/error");
        log.info("不需要权限的请求===" + JsonUtils.toJson(permitList));
        List<String> authenticatedList = oauth2Properties.getAuthenticated();
        log.info("需要权限的请求===" + JsonUtils.toJson(authenticatedList));
        http.requestMatchers()
                .and()
                .authorizeRequests()
                //设置不需要授权的请求
                .antMatchers(permitList.stream().toArray(String[]::new)).permitAll()
                //其他请求都需要授权
                .anyRequest().authenticated()
                .and()
                //在OAuth2AuthenticationProcessingFilter之前添加过滤器
                .addFilterBefore(unicornPermitAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        //身份认证异常处理
        resources.authenticationEntryPoint(new UnicornAuthExceptionEntryPoint());
    }
}
