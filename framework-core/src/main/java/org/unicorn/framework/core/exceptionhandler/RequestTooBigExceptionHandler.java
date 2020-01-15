package org.unicorn.framework.core.exceptionhandler;

import io.undertow.server.RequestTooBigException;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

/**
 * RequestTooBigException异常处理
 * @author  xiebin
 */
@Component
public class RequestTooBigExceptionHandler implements IExceptionHandler {

    @Override
    public boolean supports(Exception e) {
        if(e instanceof RequestTooBigException){
            return true;
        }else if(e.getCause() instanceof  RequestTooBigException){
            return true;
        }
        return false;
    }

    @Override
    public ResponseDto<String> handler(Exception e, String url) {
        ResponseDto  resDto =new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG);
        resDto.setUrl(url);
        return resDto;
    }
}
