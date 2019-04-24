package org.unicorn.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author  xiebin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnicornIdempotent {
    /**
     * 幂等key的分组值，例如：sale-check
     * @return
     */
    String value();

    /**
     * 参数的表达式，用来确定key值，例如："#req.saleInfo.channelCode+'-'+#req.seqId"
     * @return
     */
    String express();
}
