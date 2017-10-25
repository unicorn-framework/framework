package org.unicorn.framework.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.unicorn.framework.enums.jms.JmsCommunicationType;
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JmsCapAnnotation {
    /**
     * 消息目的地
     * @return
     */
	public String destination();
	/**
	 * 消息发布类型
	 */
	public JmsCommunicationType jmsCommunicationType() default JmsCommunicationType.P2P;
}
