package org.unicorn.framework.cache.annoation;

import java.lang.annotation.*;

import org.springframework.web.bind.annotation.Mapping;

/**
 *
 * 
 * 登陆检查
 *
 * @author xiebin
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface SessionCheck {
}
