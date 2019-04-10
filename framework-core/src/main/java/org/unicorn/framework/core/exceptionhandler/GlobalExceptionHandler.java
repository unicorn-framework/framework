package org.unicorn.framework.core.exceptionhandler;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.core.exception.UnicornRuntimeException;
import org.unicorn.framework.util.json.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xiebin
 */
@ControllerAdvice
public class GlobalExceptionHandler extends AbstractService {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDto<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ResponseDto<String> resDto = new ResponseDto<>(SysCode.SYS_FAIL,e.getMessage());
        resDto.setUrl(req.getRequestURL().toString());
        Map<String, IExceptionHandler> beanMap=SpringContextHolder.getApplicationContext().getBeansOfType(IExceptionHandler.class);
        for(String beanName:beanMap.keySet()){
            IExceptionHandler exceptionHandler=beanMap.get(beanName);
            if(exceptionHandler.supports(e)){
                return exceptionHandler.handler(e);
            }
        }
        error("异常信息:{}", JsonUtils.toJson(resDto), e);
        return resDto;
    }

}
