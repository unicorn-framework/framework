package org.unicorn.framework.oauth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 异常转换
 *
 * @author xiebin
 * @since 1.0
 */
@Component("customWebResponseExceptionTranslator")
@Slf4j
public class UnicornWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    private static final String USER_PASSWORD_ERROR = "用户信息错误";

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("异常:", e);
        if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            if (e instanceof InvalidGrantException) {
                InvalidGrantException ig=(InvalidGrantException)e;
                return ResponseEntity
                        .status(200)
                        .body(new UnicornOauthException(ig.getMessage()));
            } else {
                return ResponseEntity
                        .status(oAuth2Exception.getHttpErrorCode())
                        .body(new UnicornOauthException(USER_PASSWORD_ERROR));
            }

        } else {
            return ResponseEntity
                    .status(200)
                    .body(new UnicornOauthException(e.getMessage()));
        }

    }
}
