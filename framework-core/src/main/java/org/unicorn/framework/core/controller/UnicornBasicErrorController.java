package org.unicorn.framework.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author  xiebin
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@EnableConfigurationProperties({ServerProperties.class})
public class UnicornBasicErrorController implements ErrorController {

    private ErrorAttributes errorAttributes;

    @Autowired
    private ServerProperties serverProperties;

    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName()
            + ".ERROR";
    /**
     * 初始化ExceptionController
     * @param errorAttributes
     */
    @Autowired
    public UnicornBasicErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }

    /**
     * 定义500的错误JSON信息
     * @param request
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) throws Throwable {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode==404){
            throw new PendingException(SysCode.URL_NOT_EXIST);
        }
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        //获取并将异常抛出去
        throw getError(requestAttributes);
    }

    /**
     * 获取异常对象
     * @param requestAttributes
     * @return
     */
    public Throwable getError(RequestAttributes requestAttributes) {
        Throwable exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        if (exception == null) {
            exception = getAttribute(requestAttributes, "javax.servlet.error.exception");
        }
        return exception;
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 实现错误路径,暂时无用
     * @see ExceptionMvcAutoConfiguration#containerCustomizer()
     * @return
     */
    @Override
    public String getErrorPath() {
        return "";
    }

}