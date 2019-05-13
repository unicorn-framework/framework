package org.unicorn.framework.core.exceptionhandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
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
        //初始化返回
        ResponseDto<String> resDto = new ResponseDto<>(SysCode.SYS_FAIL,e.getMessage());
        resDto.setUrl(req.getRequestURL().toString());
        //将异常交给对应的异常分析处理器处理
        Map<String, IExceptionHandler> beanMap=SpringContextHolder.getApplicationContext().getBeansOfType(IExceptionHandler.class);
        for(String beanName:beanMap.keySet()){
            IExceptionHandler exceptionHandler=beanMap.get(beanName);
            //如果支持此处理器则进行相应的处理
            if(exceptionHandler.supports(e)){
                resDto= exceptionHandler.handler(e);
                break;
            }
        }
        error("异常信息:{}", JsonUtils.toJson(resDto), e);
        return resDto;
    }

}
