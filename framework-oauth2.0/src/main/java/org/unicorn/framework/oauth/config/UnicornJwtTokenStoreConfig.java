package org.unicorn.framework.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.unicorn.framework.oauth.properties.OAuth2Properties;
import org.unicorn.framework.oauth.security.UnironJwtTokenEnhancer;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "unicorn.security.oauth2", name = "storeType", havingValue = "jwt")
public class UnicornJwtTokenStoreConfig {


    @Autowired
    private OAuth2Properties oAuth2Properties;

    /**
     * 使用jwtTokenStore存储token
     *
     * @return
     */
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 用于生成jwt
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(oAuth2Properties.getJwtSigningKey());//生成签名的key
        return accessTokenConverter;
    }

    /**
     * 用于扩展JWT
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer() {
        return new UnironJwtTokenEnhancer();
    }


}
