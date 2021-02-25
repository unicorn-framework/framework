package org.unicorn.framework.register.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.register.offline.UnicornNacosOffline;

/**
 * @author xiebin
 * @since 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cloud.nacos.discovery", name = "server-addr", matchIfMissing = false)
public class UnicornNacosConfig {


    /**
     * 用户nacos手动下线
     *
     * @return
     */
    @Bean

    public UnicornNacosOffline unicornNacosOffline() {
        return new UnicornNacosOffline();
    }


}
