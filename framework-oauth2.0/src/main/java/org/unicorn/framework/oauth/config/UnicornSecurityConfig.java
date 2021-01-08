package org.unicorn.framework.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 安全配置
 *
 * @author xiebin
 * @ EnableWebSecurity 启用web安全
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class UnicornSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * 静态资源被拦截，不会进入安全过滤器链
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("*.html");
        web.ignoring().antMatchers("/**/**.html", "/**/**.js", "/**/**.css", "/**/**.ico", "/**/**.ttf");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/**/swagger**", "/**/v2/**");
    }


    @Override
    @Bean
    @ConditionalOnProperty(prefix = "unicorn.security.oauth2", name = "authorizationServer", havingValue = "true")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}