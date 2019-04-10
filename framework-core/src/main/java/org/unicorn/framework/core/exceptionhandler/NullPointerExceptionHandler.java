package org.unicorn.framework.core.exceptionhandler;

import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

/**
 * @author xiebin
 */
@Component
public class NullPointerExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        return (e instanceof NullPointerException);
    }

    @Override
    public ResponseDto<String> handler(Exception e) {
        return new ResponseDto<>(SysCode.SYS_NULL_POINT);
    }
}
