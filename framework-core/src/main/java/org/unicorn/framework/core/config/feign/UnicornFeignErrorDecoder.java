package org.unicorn.framework.core.config.feign;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.util.json.JsonUtils;

import java.io.IOException;

/**
 * @author xiebin
 */
@Configuration
public class UnicornFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // 这里直接拿到我们抛出的异常信息
            String message = Util.toString(response.body().asReader());
            ResponseDto  responseDto=JsonUtils.fromJson(message, ResponseDto.class);
            return  new PendingException(responseDto.getResCode(),responseDto.getResInfo());
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}
