package org.unicorn.framework.oauth.handler;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author xiebin
 * @since 1.0
 */
@JsonSerialize(using = UnicornOauthExceptionSerializer.class)
public class UnicornOauthException extends OAuth2Exception {
    public UnicornOauthException(String msg) {
        super(msg);
    }
}
