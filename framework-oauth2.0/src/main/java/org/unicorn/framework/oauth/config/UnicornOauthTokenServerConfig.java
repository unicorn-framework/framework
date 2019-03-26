package org.unicorn.framework.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.oauth.component.UnicornAccessTokenService;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties({OAuth2Properties.class})
public class UnicornOauthTokenServerConfig {
    @Autowired
    private OAuth2Properties OAuth2Properties;
    @Bean
    public UnicornAccessTokenService unicornAccessTokenService() {
        UnicornAccessTokenService unicornAccessTokenService = new UnicornAccessTokenService();
        return unicornAccessTokenService;
    }


}
