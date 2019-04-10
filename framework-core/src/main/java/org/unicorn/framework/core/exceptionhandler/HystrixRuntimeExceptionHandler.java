package org.unicorn.framework.core.exceptionhandler;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
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
    public ResponseDto<String> handler(Exception e) {
        ResponseDto<String> resDto = new ResponseDto<>(SysCode.SYS_FAIL,e.getMessage());
        HystrixRuntimeException hre=(HystrixRuntimeException)e;
        if(hre.getFallbackException().getCause() instanceof PendingException){
            PendingException pe = (PendingException) hre.getFallbackException().getCause();
            resDto.setResCode(pe.getCode());
            resDto.setResInfo(pe.getMessage());
        }else if(hre.getFallbackException().getCause() instanceof  AssertionError){
            if(hre.getFallbackException().getCause().getCause() instanceof  PendingException){
                PendingException pe = (PendingException) hre.getFallbackException().getCause().getCause();
                resDto.setResCode(pe.getCode());
                resDto.setResInfo(pe.getMessage());
            }
        }else {
            resDto.setResCode(SysCode.SYS_FAIL.getCode());
            resDto.setResInfo(e.getMessage());
        }
        return resDto;
    }
}
