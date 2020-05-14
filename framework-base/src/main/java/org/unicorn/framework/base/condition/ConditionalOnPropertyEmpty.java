package org.unicorn.framework.base.condition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 用于配置文件中属性名称对应的属性值的空值处理
 * @see ConditionalOnProperty
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(OnPropertyEmptyConditional.class)
public @interface ConditionalOnPropertyEmpty {

    /**
     * 通用属性前缀
     */
    String value();

    /**
     * 附加条件
     * <p>
     * 如果为true，则属性名称对应的值存在并且这个值为true就匹配
     * 如果为false，则属性名称对应的值不存在就匹配
     * </p>
     *
     * @return
     */
    boolean attach() default true;
}
