package org.unicorn.framework.core.config.feign;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.util.json.JsonUtils;

import java.io.IOException;

/**
 * feign客户端调用时异常转化处理
 * 对于restful抛出的4xx的错误，也许大部分是业务异常，并不是服务提供方的异常，
 * 因此在进行feign client调用的时候，需要进行errorDecoder去处理，
 * 适配为HystrixBadRequestException，好避开circuit breaker的统计，
 * 否则就容易误判，传几个错误的参数，立马就熔断整个服务了，后果不堪设想。
 * @author xiebin
 */
@Configuration
public class UnicornFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if(response.status() >= 400 && response.status() <= 499){
                return new HystrixBadRequestException("400错误");
            }
            // 这里直接拿到我们抛出的异常信息
            String message = Util.toString(response.body().asReader());
            ResponseDto  responseDto=JsonUtils.fromJson(message, ResponseDto.class);
            return  new PendingException(responseDto.getResCode(),responseDto.getResInfo());
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}
