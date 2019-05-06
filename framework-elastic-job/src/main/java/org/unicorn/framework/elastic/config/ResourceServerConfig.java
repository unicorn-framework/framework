package org.unicorn.framework.elastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.unicorn.framework.oauth.config.UnicornResourceServerConfig;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends UnicornResourceServerConfig {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();

        super.configure(http);
    }

}
