package org.unicorn.framework.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.unicorn.framework.gateway.limit.IpKeyResolver;
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
    public UnicornRateLimiterGatewayFilterFactory rateLimiterGatewayFilterFactory(RateLimiter rateLimiter) {

        return new UnicornRateLimiterGatewayFilterFactory(rateLimiter, unicornKeyResolver());
    }

    @Bean
    public KeyResolver unicornKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

//
//    public KeyResolver apiKeyResolver() {
//        return exchange ->
//                Mono.just(exchange.getRequest().getPath().value());
//    }

}
