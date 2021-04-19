
package org.unicorn.framework.cache.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.annotation.UnicornIdempotent;
import org.unicorn.framework.cache.lock.LockService;
import org.unicorn.framework.cache.util.UnicornIdempotentUtil;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

import java.util.concurrent.TimeUnit;

/**
 * 拦截被UnicornIdempotent注解的方法
 * 用来对接口进行幂等处理
 * 该方法存在时间窗口，在窗口时间内支持幂等，提交过不能再次提交，超过时间窗口之后可以再次提交
 *
 * @author xiebin
 */
@Aspect
@Component
@Slf4j
@EnableConfigurationProperties(UnicornIdempotentProperties.class)
public class UnicornIdempotentAspect {

    @Autowired
    private UnicornIdempotentProperties unicornIdempotentProperties;
    @Autowired
    private LockService lockService;

    @Before("@annotation(unicornIdempotent)")
    public void processIdempotent(JoinPoint jp, UnicornIdempotent unicornIdempotent) {
        try {
            String key = UnicornIdempotentUtil.parserIdempotentKey(unicornIdempotent.value(), jp);
            //如果不能获取锁说明时间窗口内已经提交过了，属于重复提交
            if (!lockService.tryLock(key, 0, TimeUnit.MILLISECONDS, unicornIdempotentProperties.getTimeWindowSeconds(), TimeUnit.SECONDS)) {
                throw new PendingException(SysCode.REPEATED_SUBMIT);
            }
        } catch (PendingException pe) {
            throw pe;
        } catch (Exception e) {
            log.warn("幂等性处理失败", e);
        }
    }
}
