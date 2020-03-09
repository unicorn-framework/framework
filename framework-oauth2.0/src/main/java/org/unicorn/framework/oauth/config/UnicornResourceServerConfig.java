package org.unicorn.framework.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
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

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<String> permitList = oauth2Properties.getPermitAlls();
        permitList.add("/oauth/**");
        permitList.add("/**/error");
        log.info("不需要权限===" + JsonUtils.toJson(permitList));
        List<String> authenticatedList = oauth2Properties.getAuthenticated();
        log.info("需要权限===" + JsonUtils.toJson(authenticatedList));
        http.requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers(permitList.stream().toArray(String[]::new)).permitAll()
                .antMatchers(authenticatedList.stream().toArray(String[]::new)).authenticated()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //身份认证异常处理
        resources.authenticationEntryPoint(new UnicornAuthExceptionEntryPoint());
    }
}
