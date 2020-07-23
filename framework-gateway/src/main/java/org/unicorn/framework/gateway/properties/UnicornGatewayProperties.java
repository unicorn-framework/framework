package org.unicorn.framework.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiebin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "unicorn.reactor.netty")
public class UnicornGatewayProperties {
    /**
     * IO工作线程数
     */
    private String workerCount = "5";


}
