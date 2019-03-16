package org.unicorn.framework.oauth.properties;

import lombok.Data;

/**
 *
 * @author xiebin
 * @since 1.0
 */
@Data
public class OAuth2ClientProperties {

    private String clientId;

    private String clientSecret;

    private Integer accessTokenValiditySeconds = 7200;

}
