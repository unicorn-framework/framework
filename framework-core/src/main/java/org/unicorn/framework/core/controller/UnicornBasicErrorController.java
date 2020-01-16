package org.unicorn.framework.core.controller;

import io.undertow.server.RequestTooBigException;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.dto.RequestInfoDto;
import org.unicorn.framework.core.dto.ResponseInfoDto;
import org.unicorn.framework.util.http.RequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiebin
 */
@RestController
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
    @RequestMapping(value = "/error")
    public ResponseEntity<ResponseDto<?>> error(HttpServletRequest request) throws Throwable {
        return new ResponseEntity<>(handlerException(request), HttpStatus.OK);
    }

    /**
     * 异常处理
     *
     * @param request
     * @return
     * @throws Throwable
     */
    public ResponseDto<?> handlerException(HttpServletRequest request) throws Throwable {

        HttpStatus status = this.getStatus(request);
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        ResponseDto<?> responseDto = null;
        String url = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        Throwable throwable = getError(requestAttributes);
        url = request.getRequestURL().toString().replace("/error", "") + url;
        if (status.equals(HttpStatus.UNAUTHORIZED)) {
            responseDto = new ResponseDto<>(SysCode.SESSION_ERROR);
        }
        if (status.equals(HttpStatus.NOT_FOUND)) {
            responseDto = new ResponseDto<>(SysCode.URL_NOT_EXIST);
        }
        if (status.equals(HttpStatus.TOO_MANY_REQUESTS)) {
            responseDto = new ResponseDto<>(SysCode.API_LIMIT_ERROR);
        }
        if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            if (throwable instanceof MultiPartParserDefinition.FileTooLargeException) {
                responseDto = new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG);
            } else if (throwable instanceof RequestTooBigException) {
                responseDto = new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG);
            } else if (throwable.getCause() instanceof RequestTooBigException) {
                responseDto = new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG);
            } else if (throwable.getCause() instanceof MultiPartParserDefinition.FileTooLargeException) {
                responseDto = new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG);
            }
        }
        if (responseDto != null) {
            responseDto.setUrl(url);
            return responseDto;
        }

        //其他异常获取并将异常抛出去
        throw throwable;
    }

    /**
     * 获取异常对象
     *
     * @param requestAttributes
     * @return
     */
    public Throwable getError(RequestAttributes requestAttributes) {
        Throwable exception = getAttribute(requestAttributes, "javax.servlet.error.exception");
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

//    /**
//     * 打印请求日志
//     *
//     * @param request
//     */
//    private void printlnRequestLog(HttpServletRequest request, ResponseDto<?> responseDto, String url) {
//        RequestInfoDto requestInfoDto = new RequestInfoDto();
//        //设置请求ID
//        requestInfoDto.setRequestId(request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
//        //请求方法
//        requestInfoDto.setHttpMethod(request.getMethod());
//        //请求url
//        requestInfoDto.setRequestUrl(url);
//        //请求IP
//        requestInfoDto.setRemoteIp(RequestUtils.getIp(request));
//        //打印请求日志
//        log.info("接口请求信息：{}", requestInfoDto);
//
//        ResponseInfoDto responseInfoDto = new ResponseInfoDto();
//        //设置响应ID
//        responseInfoDto.setResponseId(request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
//        //设置响应报文
//        responseInfoDto.setResponseBody(responseDto);
//        //打印响应日志
//        log.info("接口响应信息：{}", responseInfoDto);
//    }
}