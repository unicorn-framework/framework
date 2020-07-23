package org.unicorn.framework.web.config.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.constants.Constants;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.util.json.JsonUtils;

import java.io.IOException;

/**
 * 接口提供方抛出的异常进行转换
 * 当调用服务时，如果服务返回的状态码不是200，就会进入到Feign的ErrorDecoder中
 * feign客户端调用时异常转化处理
 * 对于restful抛出的4xx的错误，也许大部分是业务异常，并不是服务提供方的异常，
 * 因此在进行feign client调用的时候，需要进行errorDecoder去处理，
 * 适配为HystrixBadRequestException，好避开circuit breaker的统计，
 * 否则就容易误判，传几个错误的参数，立马就熔断整个服务了，后果不堪设想。
 *
 * @author xiebin
 */
@Configuration
@Slf4j
public class UnicornFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        log.info("feign 调用方法名===》" + methodKey);
        try {
            if (response.status() == Constants.HTTP_401) {
                return new PendingException(SysCode.SESSION_ERROR);
            }
            if (response.status() == Constants.HTTP_403) {
                return new PendingException(SysCode.UNAUTHOR__ERROR);
            }
            if (response.body() == null) {
                return new PendingException(SysCode.SYS_FAIL);
            }
            return getResException(methodKey, response);
        } catch (Exception e) {
            log.error("feign错误", e);
            return new PendingException(SysCode.SYS_FAIL, "feignException", e);
        }
    }

    /**
     * @param methodKey
     * @param response
     * @return
     * @throws IOException
     */
    private Exception getResException(String methodKey, Response response) throws IOException {
        // 这里直接拿到feign接口服务端抛出的异常信息
        String message = Util.toString(response.body().asReader());
        //转换成ResponseDto对象
        ResponseDto responseDto = JsonUtils.fromJson(message, ResponseDto.class);
        //4XX 一般是客服端请求不合法，返回HystrixBadRequestException,不进行熔断
        if (HttpStatus.valueOf(response.status()).is4xxClientError()) {
            //
            if (methodKey.contains(Constants.AUTH__METHOD_KEY)) {
                return new HystrixBadRequestException("用户信息错误");
            }
            return new HystrixBadRequestException("请求参数错误 :" + methodKey);
        }
        return new PendingException(responseDto);
    }
}
