package org.unicorn.framework.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.unicorn.framework.core.ResBean;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.gateway.code.GatewayCode;
import org.unicorn.framework.util.common.DateUtils;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * gateway webflux 全局异常处理
 *
 * @author xiebin
 */
@Slf4j
public class UnicornGatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public UnicornGatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 确定返回什么HttpStatus，统一返回200
     *
     * @param errorAttributes
     * @return
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        return HttpStatus.OK;
    }

    @Override
    protected Mono<ServerResponse> renderErrorView(ServerRequest request) {
        boolean includeStackTrace = this.isIncludeStackTrace(request, MediaType.TEXT_HTML);
        Map<String, Object> error = this.getErrorAttributes(request, includeStackTrace);
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(error));
    }

    /**
     * 返回的错误信息json内容
     *
     * @param request
     * @param includeStackTrace
     * @return
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable error = this.getError(request);
        ResBean resBean = genResBean(error);
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("resCode", resBean.getCode());
        errorAttributes.put("resInfo", resBean.getInfo());
        errorAttributes.put("url", request.path());
        errorAttributes.put("data", null);
        errorAttributes.put("responseTime", DateUtils.formatDatetime(new Date()));
        log.error("访问接口{}时出现异常", request.path(), error);
        return errorAttributes;

    }

    private ResBean genResBean(Throwable error) {
        HttpStatus errorStatus = this.determineHttpStatus(error);
        if (errorStatus.equals(HttpStatus.NOT_FOUND)) {
            return SysCode.URL_NOT_EXIST;
        }
        if (error instanceof PendingException) {
            PendingException pe = (PendingException) error;
            return ResBean.fromPendingException(pe);
        }
        if (error.getCause() instanceof PendingException) {
            PendingException pe = (PendingException) error.getCause();
            return ResBean.fromPendingException(pe);
        }
        if (error instanceof IllegalStateException) {
            if (error.getMessage().contains("Invalid host: lb:")) {
                return GatewayCode.INVALID_HOST;
            }
        }
        if(error instanceof NotFoundException){
            if (error.getMessage().contains("SERVICE_UNAVAILABLE ")) {
                return GatewayCode.INVALID_HOST;
            }
        }
        //返回默认
        return SysCode.SYS_FAIL;
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        return error instanceof ResponseStatusException ? ((ResponseStatusException) error).getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
