package org.unicorn.framework.core.exceptionhandler;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

/**
 * @author xiebin
 */
@Component
public class HttpMediaTypeNotSupportedExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        return (e instanceof HttpMediaTypeNotSupportedException);
    }

    @Override
    public ResponseDto<String> handler(Exception e,String url) {
        ResponseDto  resDto =new ResponseDto<>(SysCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED);
        resDto.setUrl(url);
        return resDto;
    }
}
