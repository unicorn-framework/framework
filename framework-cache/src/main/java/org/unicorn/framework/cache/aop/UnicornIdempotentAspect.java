
package org.unicorn.framework.cache.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.unicorn.framework.cache.annotation.UnicornIdempotent;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.cache.lock.LockService;
import org.unicorn.framework.cache.util.UnicornIdempotentUtil;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.web.utils.web.RequestUtil;

import javax.servlet.http.HttpServletRequest;
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
    public static final String IDEMPOTENT_NAME_SPACE="idempotent:ns:";

    @Autowired
    private UnicornIdempotentProperties unicornIdempotentProperties;
    @Autowired
    private LockService lockService;
    @Autowired
    private CacheService cacheService;

    @Around("@annotation(unicornIdempotent)")
    public Object processIdempotent(ProceedingJoinPoint jp, UnicornIdempotent unicornIdempotent) throws Throwable {
        try {
            //获取请求参数
            Object[] args = jp.getArgs();
            HttpServletRequest request = RequestUtil.getRrequest();
            String uri=request.getServletPath();
            //获取key
            String key = UnicornIdempotentUtil.parserIdempotentKey(args, uri,unicornIdempotent.value(), jp);
            //如果不能获取锁说明时间窗口内已经提交过了，属于重复提交
            if (!lockService.tryLock(key, 0, TimeUnit.MILLISECONDS, unicornIdempotentProperties.getTimeWindowSeconds(), TimeUnit.SECONDS)) {
                if (isThrowException(unicornIdempotent)) {
                    throw new PendingException(SysCode.REPEATED_SUBMIT, unicornIdempotentProperties.getTips());
                }
                log.info("返回上一次调用缓存结果:{}",uri);
                //在缓存中获取执行结果
                return cacheService.get(key, IDEMPOTENT_NAME_SPACE);
            }
            //调用目标方法
            Object result = jp.proceed(args);
            //如果不抛异常
            if (!isThrowException(unicornIdempotent)) {
                cacheService.put(key, result, unicornIdempotentProperties.getTimeWindowSeconds(), TimeUnit.SECONDS, IDEMPOTENT_NAME_SPACE);
            }
            return result;
        } catch (PendingException pe) {
            throw pe;
        } catch (Exception e) {
            log.warn("幂等性处理失败", e);
            throw new PendingException(SysCode.SYS_FAIL);
        }
    }

    /**
     * 是否抛出异常
     *
     * @param unicornIdempotent
     * @return
     */
    private boolean isThrowException(UnicornIdempotent unicornIdempotent) {
        //全局
        return unicornIdempotentProperties.isGlobleThrowException() || unicornIdempotent.throwException();
    }

}
