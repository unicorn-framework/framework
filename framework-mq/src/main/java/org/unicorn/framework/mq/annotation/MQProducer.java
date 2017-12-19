package org.unicorn.framework.mq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author xiebin
 * RocketMQ生产者自动装配注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MQProducer {
    String topic() default "";
    String tag() default "";
}
