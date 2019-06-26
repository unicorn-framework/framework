package org.unicorn.framework.core.config.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.FeignException;
import org.unicorn.framework.util.json.JsonUtils;

/**
 * 接口提供方抛出的异常进行转换
 * feign客户端调用时异常转化处理
 * 对于restful抛出的4xx的错误，也许大部分是业务异常，并不是服务提供方的异常，
 * 因此在进行feign client调用的时候，需要进行errorDecoder去处理，
 * 适配为HystrixBadRequestException，好避开circuit breaker的统计，
 * 否则就容易误判，传几个错误的参数，立马就熔断整个服务了，后果不堪设想。
 * @author xiebin
 */
@Configuration
@Slf4j
public class UnicornFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String message=null;
        try {
            if(response.status() >= 401){
                return new PendingException(SysCode.SESSION_ERROR);
            }
            if(response.status() >= 403){
                return new PendingException(SysCode.UNAUTHOR__ERROR);
            }
            if(response.status() >= 400 && response.status() <= 499){
                if(methodKey.contains("AuthTokenClient#postAccessToken(String,Map)")){
                    return new HystrixBadRequestException("用户信息错误");
                }
                return new HystrixBadRequestException("请求参数错误");
            }
            // 这里直接拿到feign服务端抛出的异常信息
            message = Util.toString(response.body().asReader());
            log.info("feign 异常信息===》"+message);
            ResponseDto  responseDto=JsonUtils.fromJson(message, ResponseDto.class);
            return  new FeignException(responseDto);
        } catch (Exception e) {
            log.error("feign错误",e);
            return  new FeignException(SysCode.SYS_FAIL,e);
        }
    }
}
