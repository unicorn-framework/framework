package org.unicorn.framework.core.exceptionhandler;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.unicorn.framework.base.base.AbstractBaseController;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.util.json.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author xiebin
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 不需要弹出来的 返回码列表
     */
    List<String> resCodeList=Lists.newArrayList();
    {   //token失效
        resCodeList.add(SysCode.SESSION_ERROR.getCode());
        //接口不存在
        resCodeList.add(SysCode.URL_NOT_EXIST.getCode());
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDto<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        //初始化返回  //tips：默认是false
        ResponseDto<String> resDto = new ResponseDto<>(SysCode.SYS_FAIL);
        String url=req.getRequestURL().toString();
        resDto.setUrl(url);
        //将异常交给对应的异常分析处理器处理
        Map<String, IExceptionHandler> beanMap=SpringContextHolder.getApplicationContext().getBeansOfType(IExceptionHandler.class);
        for(String beanName:beanMap.keySet()){
            IExceptionHandler exceptionHandler=beanMap.get(beanName);
            //如果支持此处理器则进行相应的处理
            if(exceptionHandler.supports(e)){
                resDto= exceptionHandler.handler(e,url);
                break;
            }
        }
        log.error("异常信息:{}", JsonUtils.toJson(resDto), e);
        return resDto;
    }
}
