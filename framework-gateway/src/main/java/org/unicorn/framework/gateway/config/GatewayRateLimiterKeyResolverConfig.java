package org.unicorn.framework.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.unicorn.framework.gateway.limit.UnicornRateLimiterGatewayFilterFactory;
import reactor.core.publisher.Mono;

/**
 * gateway 路由统一降级配置类
 *
 * @author xiebin
 */
@Slf4j
@Configuration
public class GatewayRateLimiterKeyResolverConfig {
    @Bean
    public UnicornRateLimiterGatewayFilterFactory rateLimiterGatewayFilterFactory(RateLimiter rateLimiter, KeyResolver ipKeyResolver) {

        return new UnicornRateLimiterGatewayFilterFactory(rateLimiter, ipKeyResolver);
    }

    /**
     * 默认的key策略
     *
     * @return
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            log.info("ipKeyResolver");
            return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
        };
    }

    /**
     * 接口级别限流
     *
     * @return
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> {
            log.info("apiKeyResolver");
            return Mono.just(exchange.getRequest().getPath().value());
        };

    }

    /**
     * 用户级别限流
     *
     * @return
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            log.info("userKeyResolver");
            return Mono.just(exchange.getRequest().getHeaders().getFirst("Authorization"));
        };
    }

}
