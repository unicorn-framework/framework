package org.unicorn.framework.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解
 *
 * @author xiebin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnicornIdempotent {
    /**
     * 幂等key的分组值
     *
     * @return
     */
    String group() default "unicorn";

    /**
     * 参数的表达式，用来确定key值，例如："#req.saleInfo.channelCode+'-'+#req.seqId"
     *
     * @return
     */
    String value();


}
