package org.unicorn.framework.core.exceptionhandler;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import feign.RetryableException;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

/**
 * hystrix异常处理
 * @author  xiebin
 */
public class UnicornHystrixExceptionHandler {

    public void hystrixExceptionHandler( Throwable throwable) throws PendingException {
        if (throwable instanceof PendingException) {
            throw (PendingException) throwable;
        }else if(throwable instanceof HystrixTimeoutException){
            throw new PendingException(SysCode.SYS_FAIL, "调用超时");
        }else if(throwable.getCause() instanceof RetryableException){
            throw new PendingException(SysCode.SYS_FAIL, "没有找到可用的服务");
        }else {
            throw new PendingException(SysCode.SYS_FAIL, throwable.getMessage());
        }
    }
}
