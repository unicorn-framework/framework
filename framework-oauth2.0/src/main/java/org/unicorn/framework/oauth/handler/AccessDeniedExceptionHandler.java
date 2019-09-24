package org.unicorn.framework.oauth.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exceptionhandler.IExceptionHandler;



/**
 * @author xiebin
 */
@Component
public class AccessDeniedExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        if (e instanceof AccessDeniedException) {
            return true;
        } else if (e.getCause() instanceof AccessDeniedException) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseDto<String> handler(Exception e,String url) {
        ResponseDto  resDto =new ResponseDto<>(SysCode.UNAUTHOR__ERROR);
        resDto.setUrl(url);
        return resDto;
    }
}
