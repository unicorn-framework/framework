package org.unicorn.framework.oauth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author xieibn
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "unicorn.security.oauth2")
public class OAuth2Properties {
    /**
     * 默认 jwt签名key
     */
    private String jwtSigningKey = "unicorn";

    private OAuth2ClientProperties[] clients = {};
}
