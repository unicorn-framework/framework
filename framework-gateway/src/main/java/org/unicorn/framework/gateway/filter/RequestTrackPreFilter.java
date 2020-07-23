package org.unicorn.framework.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.utils.IdGeneratorSingleton;
import reactor.core.publisher.Mono;

/**
 * @author xiebin
 * 请求跟踪过滤器
 * 所有的资源请求在路由之前进行前置过滤 生成请求唯一id并传递给下游服务
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestTrackPreFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //设置请求头，记得build
        ServerHttpRequest host = exchange.getRequest().mutate().header(UnicornConstants.REQUEST_TRACK_HEADER_NAME, "request:track:" + IdGeneratorSingleton.getInstance().generateKey().longValue()).build();
        //将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }
}