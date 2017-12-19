package org.unicorn.framework.mq.annotation;

import java.lang.annotation.*;

/**
 * 启动rocketMq配置
 * @author xiebin
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableMQConfiguration {
}
