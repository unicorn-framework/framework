package org.unicorn.framework.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.gateway.dto.BaseSecurityDto;
import org.unicorn.framework.gateway.handler.GatewayHandlerAdapter;
import org.unicorn.framework.gateway.handler.IGatewayHandler;
import org.unicorn.framework.gateway.properties.UnicornGatewaySecurityProperties;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * @author xiebin
 * 资源安全过滤器
 * 所有的资源请求在路由之前进行前置过滤
 */
@Slf4j
public class SecurityFilter implements GlobalFilter {
    @Autowired
    private UnicornGatewaySecurityProperties unicornGatewaySecurityProperties;

    private AntPathMatcher antPathMatcher;

    private List<String> ignoreUrls = Lists.newArrayList();

    public SecurityFilter() {
        this.antPathMatcher = new AntPathMatcher();

    }

    /**
     * 配置需要忽略安全检查的url
     */
    @PostConstruct
    public void initIgnoreUrls() {
        ignoreUrls.add("/static/**");
        ignoreUrls.add("/**/*.html");
        ignoreUrls.add("/**/*.js");
        ignoreUrls.add("/**/*.css");
        ignoreUrls.add("/**/*.ico");
        ignoreUrls.add("/**/*.ttf");
        ignoreUrls.add("/**/*.ico");
        ignoreUrls.add("/**/static/**");
        ignoreUrls.add("/**/swagger**");
        ignoreUrls.add("/**/v2/**");
        ignoreUrls.addAll(unicornGatewaySecurityProperties.getIgnoreUrls());
    }

    public List<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) throws PendingException {
        if (shouldFilter(exchange)) {
            invoker(exchange);
        }
        return chain.filter(exchange);

    }

    /**
     * 过滤器是否会被执行
     *
     * @return true
     */

    public boolean shouldFilter(ServerWebExchange exchange) {
        try {

            //未开启则直接返回false
            if (!unicornGatewaySecurityProperties.getEnable()) {
                return false;
            }
            String ignoreSignHead = exchange.getRequest().getHeaders().getFirst("ignoreSignHead");
            if (unicornGatewaySecurityProperties.getIgnoreSignHeadValue().equalsIgnoreCase(ignoreSignHead)) {
                return false;
            }
            String requestUrl = exchange.getRequest().getPath().pathWithinApplication().value();
            //获取忽略url
            List<String> ignoreUrls = getIgnoreUrls();
            boolean flag = true;
            for (String pattern : ignoreUrls) {
                if (this.antPathMatcher.match(pattern, requestUrl)) {
                    flag = false;
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            log.error("是否需要安全检查异常:", e);
            return true;
        }
    }

    /**
     * 过滤逻辑
     *
     * @param exchange
     * @return 过滤结果
     */

    public void invoker(ServerWebExchange exchange) throws PendingException {
        try {
            //构造 BaseSecurityDto
            BaseSecurityDto baseSecurityDto = genBaseSecurityDto(exchange);
            //参数检查
            baseSecurityDto.vaildatioinThrowException();
            // 轮询处理
            pollingHandler(baseSecurityDto);
        } catch (PendingException pe) {
            log.error("安全拦截异常:", pe);
            throw pe;
        } catch (Exception e) {
            log.error("安全拦截异常:", e);
            throw new PendingException(SysCode.API_SECURITY_ERROR);
        }
    }

    /**
     * 轮询处理
     *
     * @param baseSecurityDto
     * @throws PendingException
     */
    private void pollingHandler(BaseSecurityDto baseSecurityDto) throws PendingException {
        //获取处理队列
        List<IGatewayHandler<BaseSecurityDto>> list = GatewayHandlerAdapter.handlerList(baseSecurityDto);
        //队列排序
        Collections.sort(list, (a, b) -> a.order().compareTo(b.order()));
        //轮询处理
        list.forEach(securityHandler -> {
            securityHandler.execute(baseSecurityDto);
        });
    }

    /**
     * 构造验证对象
     *
     * @param exchange
     * @return
     */
    private BaseSecurityDto genBaseSecurityDto(ServerWebExchange exchange) throws PendingException, Exception {
        ServerHttpRequest request = exchange.getRequest();
        log.info("url====" + request.getPath().pathWithinApplication().value());
        //appKey
        String appKey = unicornGatewaySecurityProperties.getAppKey();
        //timestamp 时间戳
        String timestamp = request.getHeaders().getFirst("timestamp");
        //随机字符串
        String nonceStr = request.getHeaders().getFirst("nonceStr");
        //sign 签名
        String sign = request.getHeaders().getFirst("sign");
        BaseSecurityDto baseSecurityDto = BaseSecurityDto.builder()
                .sign(sign)
                .timestamp(timestamp)
                .appKey(appKey)
                .nonceStr(nonceStr).build();
        log.info("baseHeader==" + baseSecurityDto.toString());
        return baseSecurityDto;
    }
}