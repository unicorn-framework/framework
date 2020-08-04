package org.unicorn.framework.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.util.json.JsonUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author xiebin
 * 请求限流过滤器
 */
@Slf4j
public class RequestLimitGatewayFilter implements GatewayFilter {
    /**
     * key处理
     */
    private KeyResolver resolver;
    /**
     * 限流器
     */
    private RateLimiter<Object> limiter;

    public RequestLimitGatewayFilter(KeyResolver resolver, RateLimiter<Object> limiter) {
        this.resolver = resolver;
        this.limiter = limiter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        return resolver.resolve(exchange).flatMap(key ->
                limiter.isAllowed(route.getId(), key).flatMap(response -> {
                    for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                        exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
                    }
                    if (response.isAllowed()) {
                        return chain.filter(exchange);
                    }
                    ServerHttpResponse rs = exchange.getResponse();
                    ResponseDto responseDto = new ResponseDto(SysCode.API_LIMIT_ERROR);
                    byte[] data = JsonUtils.toJson(responseDto).getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = rs.bufferFactory().wrap(data);
                    rs.setStatusCode(HttpStatus.OK);
                    rs.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                    return rs.writeWith(Mono.just(buffer));
                }));
    }
}