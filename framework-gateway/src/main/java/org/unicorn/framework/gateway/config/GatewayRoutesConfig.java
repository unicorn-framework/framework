package org.unicorn.framework.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.unicorn.framework.gateway.fallback.HystrixFallbackHandler;

/**
 * @author xiebin
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class GatewayRoutesConfig {

    private final HystrixFallbackHandler hystrixFallbackHandler;

    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.path("/defaultFallback")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), hystrixFallbackHandler);
    }
}
