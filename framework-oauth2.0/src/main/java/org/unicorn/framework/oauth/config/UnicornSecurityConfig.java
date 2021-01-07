package org.unicorn.framework.oauth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.unicorn.framework.oauth.properties.OAuth2Properties;
import org.unicorn.framework.util.json.JsonUtils;

import java.util.List;

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
    @Autowired
    private OAuth2Properties oauth2Properties;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        List<String> permitList = oauth2Properties.getPermitAlls();
        permitList.add("/oauth/**");
        permitList.add("/**/error");
        log.info("需要忽略token安全检查===" + JsonUtils.toJson(permitList));
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("*.html");
        web.ignoring().antMatchers("/**/**.html", "/**/**.js", "/**/**.css", "/**/**.ico", "/**/**.ttf");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/**/swagger**", "/**/v2/**");
        web.ignoring().antMatchers(permitList.stream().toArray(String[]::new));


    }

    @Override
    @ConditionalOnProperty(prefix = "unicorn.security.oauth2", name = "authorizationServer", havingValue = "true")
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}