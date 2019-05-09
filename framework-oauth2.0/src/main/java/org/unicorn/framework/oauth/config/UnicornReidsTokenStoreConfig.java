package org.unicorn.framework.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.unicorn.framework.oauth.store.UnicornRedisTokenStore;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
//@ConditionalOnExpression("${unicorn.security.oauth2.authorizationServer:false}")
@ConditionalOnProperty(prefix = "unicorn.security.oauth2", name = "storeType", havingValue = "redis")
public class UnicornReidsTokenStoreConfig {
    /**
     * redis连接工厂
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 用于存放token
     *
     * @return
     */

    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
}
