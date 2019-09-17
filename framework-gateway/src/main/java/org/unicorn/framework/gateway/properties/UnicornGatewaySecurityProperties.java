package org.unicorn.framework.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author  xiebin
 */
@Data
@Configuration
@ConfigurationProperties(prefix="unicorn.security")
public class UnicornGatewaySecurityProperties {
    /**
     *相差分钟数
     */
    private int diffMinutes=5;

    /**
     *  密钥对   public、private
     *
     */
    private Map<String,String> keyPair;
    /**
     * 是否开启安全过滤 默认不开启
     */
    private Boolean enable=false;

}
