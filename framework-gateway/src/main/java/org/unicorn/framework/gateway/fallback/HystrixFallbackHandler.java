package org.unicorn.framework.gateway.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import reactor.core.publisher.Mono;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-22 14:08
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(new ResponseDto<>(SysCode.MICRO_SERVICE_ERROR)));
    }
}
