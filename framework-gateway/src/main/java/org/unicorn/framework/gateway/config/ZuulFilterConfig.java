package org.unicorn.framework.gateway.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.gateway.filter.GrayscaleFilter;
import org.unicorn.framework.gateway.filter.RequestTrackPreFilter;
import org.unicorn.framework.gateway.filter.SecurityFilter;

/**
 * @author xiebin
 */
@Configuration
public class ZuulFilterConfig {

    /**
     * 灰度过滤器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "unicorn.gray", name = "enable", havingValue = "true")
    public GrayscaleFilter grayscaleFilter() {
        return new GrayscaleFilter();
    }


    /**
     * 安全过滤器
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "unicorn.security", name = "enable", havingValue = "true")
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    /**
     * 链路跟踪过滤器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "unicorn.track", name = "enable", havingValue = "true")
    public RequestTrackPreFilter requestTrackFilter() {
        return new RequestTrackPreFilter();
    }
}
