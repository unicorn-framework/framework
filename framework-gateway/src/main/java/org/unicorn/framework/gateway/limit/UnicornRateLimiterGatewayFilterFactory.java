package org.unicorn.framework.gateway.limit;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.http.HttpStatus;
import org.unicorn.framework.gateway.filter.RequestLimitGatewayFilter;

/**
 * 自定义GatewayFitlerFactory
 * 注意  xxxxGatewayFilterFactory 命名方式
 * 默认xxxx前缀为配置名称
 * eg:spring.cloud.gateway.default-filters[1].name=UnicornRateLimiter
 * 可以重写 name()方法，指定配置名称
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-31 18:25
 */
public class UnicornRateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<UnicornRateLimiterGatewayFilterFactory.Config> {
    private final RateLimiter defaultRateLimiter;
    private final KeyResolver defaultKeyResolver;

    public UnicornRateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter,
                                                  KeyResolver defaultKeyResolver) {
        super(Config.class);
        this.defaultRateLimiter = defaultRateLimiter;
        this.defaultKeyResolver = defaultKeyResolver;
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return configValue != null ? configValue : defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = (KeyResolver) this.getOrDefault(config.keyResolver, this.defaultKeyResolver);
        RateLimiter<Object> limiter = (RateLimiter) this.getOrDefault(config.rateLimiter, this.defaultRateLimiter);
        return new RequestLimitGatewayFilter(resolver, limiter);
    }

    public static class Config {
        private KeyResolver keyResolver;
        private RateLimiter rateLimiter;
        private HttpStatus statusCode = HttpStatus.TOO_MANY_REQUESTS;

        public KeyResolver getKeyResolver() {
            return keyResolver;
        }

        public Config setKeyResolver(KeyResolver keyResolver) {
            this.keyResolver = keyResolver;
            return this;
        }

        public RateLimiter getRateLimiter() {
            return rateLimiter;
        }

        public Config setRateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        public HttpStatus getStatusCode() {
            return statusCode;
        }

        public Config setStatusCode(HttpStatus statusCode) {
            this.statusCode = statusCode;
            return this;
        }
    }

}