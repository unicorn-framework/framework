package org.unicorn.framework.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.dto.RequestInfoDto;
import org.unicorn.framework.core.dto.ResponseInfoDto;
import org.unicorn.framework.util.http.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xiebin
 */
@Controller
@Slf4j
public class UnicornBasicErrorController extends AbstractErrorController {


    private final ErrorProperties errorProperties;

    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    @Autowired
    public UnicornBasicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    /**
     * 定义500的错误JSON信息
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/error")
    @ResponseBody
    public ResponseEntity<ResponseDto<?>> getError(HttpServletRequest request) throws Throwable {
        HttpStatus status = this.getStatus(request);
        return new ResponseEntity<>(handlerException(request), status);
    }

    /**
     * 异常处理
     *
     * @param request
     * @return
     * @throws Throwable
     */
    public ResponseDto<?> handlerException(HttpServletRequest request) throws Throwable {
        Map<String, Object> body = this.getErrorAttributes(request, this.isIncludeStackTrace(request, MediaType.ALL));
        ResponseDto<?> responseDto = null;
        Integer statusCode = Integer.valueOf(body.get("status").toString());
        String url = request.getRequestURL().toString().replace("/error", "") + body.get("path").toString();
        if (statusCode == 401) {
            responseDto = new ResponseDto<>(SysCode.SESSION_ERROR);
            responseDto.setUrl(url);
        }
        if (statusCode == 404) {
            responseDto = new ResponseDto<>(SysCode.URL_NOT_EXIST);
            responseDto.setUrl(url);
        }
        if (statusCode == 429) {
            responseDto = new ResponseDto<>(SysCode.API_LIMIT_ERROR);
            responseDto.setUrl(url);
        }
        if (responseDto != null) {
            printlnRequestLog(request, responseDto,url);
            return responseDto;
        }
        //其他异常获取并将异常抛出去
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        throw getError(requestAttributes);
    }

    /**
     * 定义500的错误JSON信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/error")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postError(HttpServletRequest request) throws Throwable {
        handlerException(request);
        return null;
    }

    /**
     * 获取异常对象
     *
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

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }


    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        } else {
            return include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM ? this.getTraceParameter(request) : false;
        }
    }

    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    /**
     * 打印请求日志
     *
     * @param request
     */
    private void printlnRequestLog(HttpServletRequest request, ResponseDto<?> responseDto, String url) {
        RequestInfoDto requestInfoDto = new RequestInfoDto();
        //设置请求ID
        requestInfoDto.setRequestId(request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
        //请求方法
        requestInfoDto.setHttpMethod(request.getMethod());
        //请求url
        requestInfoDto.setRequestUrl(url);
        //请求IP
        requestInfoDto.setRemoteIp(RequestUtils.getIp(request));
        //打印请求日志
        log.info("接口请求信息：{}", requestInfoDto);

        ResponseInfoDto responseInfoDto = new ResponseInfoDto();
        //设置响应ID
        responseInfoDto.setResponseId(request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
        //设置响应报文
        responseInfoDto.setResponseBody(responseDto);
        //打印响应日志
        log.info("接口响应信息：{}", responseInfoDto);
    }
}