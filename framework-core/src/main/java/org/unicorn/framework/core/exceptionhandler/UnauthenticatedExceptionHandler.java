package org.unicorn.framework.core.exceptionhandler;

import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

/**
 * @author xiebin
 */
@Component
public class UnauthenticatedExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        return (e instanceof UnauthenticatedException);
    }

    @Override
    public ResponseDto<String> handler(Exception e) {
        return new ResponseDto<>(SysCode.SESSION_ERROR);
    }
}
