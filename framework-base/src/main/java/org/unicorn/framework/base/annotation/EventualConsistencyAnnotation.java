package org.unicorn.framework.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.unicorn.framework.enums.jms.JmsCommunicationType;

/**最终一致性保障注解
 * 
 * @author xiebin
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface EventualConsistencyAnnotation {
    /**
     * 消息目的地
     * @return
     */
	public String destination();
	/**
	 * 消息通讯类型  P2P、PUBLISH_SUBSCRIBE
	 */
	public JmsCommunicationType jmsCommunicationType() default JmsCommunicationType.P2P;
}
