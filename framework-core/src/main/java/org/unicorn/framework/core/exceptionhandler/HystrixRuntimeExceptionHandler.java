package org.unicorn.framework.core.exceptionhandler;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.constants.Constants;
import org.unicorn.framework.core.exception.PendingException;

/**
 * @author xiebin
 */
@Component
public class HystrixRuntimeExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        return (e instanceof HystrixRuntimeException);
    }

    @Override
    public ResponseDto<String> handler(Exception e,String url) {
        ResponseDto  resDto =new ResponseDto<>(SysCode.HYSTRIX_EXCEPTION_MESSSGE);
        if(e.getCause() instanceof  PendingException){
            PendingException pe=(PendingException)e.getCause();
            resDto.setResCode(pe.getCode());
            resDto.setResInfo(pe.getMessage());
        }
        resDto.setUrl(url);
        return resDto;
    }
}
