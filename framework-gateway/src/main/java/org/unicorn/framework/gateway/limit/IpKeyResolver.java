package org.unicorn.framework.gateway.limit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-08-03 17:05
 */
public class IpKeyResolver implements KeyResolver {
    public static final String BEAN_NAME = "ipKeyResolver";

    public IpKeyResolver() {
    }

    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
