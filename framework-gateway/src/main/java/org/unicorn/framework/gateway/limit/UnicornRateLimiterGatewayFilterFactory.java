package org.unicorn.framework.gateway.limit;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.util.json.JsonUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    public KeyResolver getDefaultKeyResolver() {
        return defaultKeyResolver;
    }

    public RateLimiter getDefaultRateLimiter() {
        return defaultRateLimiter;
    }

    private <T> T getOrDefault(T configValue, T defaultValue) {
        return configValue != null ? configValue : defaultValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GatewayFilter apply(Config config) { KeyResolver resolver = (KeyResolver) this.getOrDefault(config.keyResolver, this.defaultKeyResolver);
        RateLimiter<Object> limiter = (RateLimiter) this.getOrDefault(config.rateLimiter, this.defaultRateLimiter);
        return (exchange, chain) -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);

            return resolver.resolve(exchange).flatMap(key ->
                    // TODO: if key is empty?
                    limiter.isAllowed(route.getId(), key).flatMap(response -> {

                        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                            exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                        }

                        if (response.isAllowed()) {
                            return chain.filter(exchange);
                        }
                        ServerHttpResponse rs = exchange.getResponse();
                        ResponseDto responseDto = new ResponseDto(SysCode.API_LIMIT_ERROR);
                        byte[] datas = JsonUtils.toJson(responseDto).getBytes(StandardCharsets.UTF_8);
                        DataBuffer buffer = rs.bufferFactory().wrap(datas);
                        rs.setStatusCode(HttpStatus.OK);
                        rs.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                        return rs.writeWith(Mono.just(buffer));
                    }));
        };
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