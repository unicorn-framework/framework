package org.unicorn.framework.oauth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author xiebin
 * @since 1.0
 */
@Component("customWebResponseExceptionTranslator")
@Slf4j
public class UnicornWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("异常:",e);
        if(e instanceof OAuth2Exception){
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new UnicornOauthException(oAuth2Exception.getMessage()));
        }else{
            return ResponseEntity
                    .status(400)
                    .body(new UnicornOauthException(e.getMessage()));
        }

    }
}
