package org.unicorn.framework.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.unicorn.framework.oauth.filter.UnicornPermitAuthenticationFilter;

/**
 * @author xiebin
 */
@Configuration
public class Oauth2BeanConfig {

    @Autowired
    @Qualifier("redisTokenStore")
    private TokenStore tokenStore;

    @Bean
    public UnicornPermitAuthenticationFilter unicornPermitAuthenticationFilter() {
        UnicornPermitAuthenticationFilter unicornPermitAuthenticationFilter = new UnicornPermitAuthenticationFilter();
        unicornPermitAuthenticationFilter.setTokenStore(tokenStore);
        return unicornPermitAuthenticationFilter;
    }


}
