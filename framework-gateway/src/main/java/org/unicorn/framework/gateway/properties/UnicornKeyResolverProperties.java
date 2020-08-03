package org.unicorn.framework.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "spring.cloud.gateway.filter.request-rate-limiter")
public class UnicornKeyResolverProperties {

    private String key = "5";


}
