package org.unicorn.framework.web.exceptionhandler;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exceptionhandler.IExceptionHandler;

/**
 * @author xiebin
 */
@Component
public class HttpMethodNotSupportedExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        return (e instanceof HttpRequestMethodNotSupportedException);
    }

    @Override
    public ResponseDto<String> handler(Exception e, String url) {
        ResponseDto resDto = new ResponseDto<>(SysCode.HTTP_METHOD_NOT_SUPPORTED);
        resDto.setUrl(url);
        return resDto;
    }
}
