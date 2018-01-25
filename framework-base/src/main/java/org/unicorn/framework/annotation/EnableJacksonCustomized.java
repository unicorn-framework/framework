package org.unicorn.framework.annotation;

import java.lang.annotation.*;

import org.springframework.web.bind.annotation.Mapping;

/**
 *
 * 
 * 是否开启json定制 
 *
 * @author xiebin
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface EnableJacksonCustomized {
}
