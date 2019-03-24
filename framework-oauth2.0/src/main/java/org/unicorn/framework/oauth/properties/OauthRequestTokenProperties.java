package org.unicorn.framework.oauth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xieibn
 * @since 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "unicorn.oauth2.header")
public class OauthRequestTokenProperties {


    /**
     * 请求头名称
     */
    private String tokenName = "Authorization";

}
