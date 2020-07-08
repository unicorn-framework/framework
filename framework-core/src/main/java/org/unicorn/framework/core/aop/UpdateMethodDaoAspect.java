
package org.unicorn.framework.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.exception.PendingException;

/**
 * @author xiebin
 */
@Aspect
@Component
@Slf4j
public class UpdateMethodDaoAspect extends DateForDaoBaseAspect {
    @Pointcut("execution(* *..dao..*.update*(..))")
    public void daoPointCut() {
    }


    @Before("daoPointCut()")
    public void processTx(JoinPoint jp) throws PendingException {
        Object args[] = jp.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        if (args.length == 1) {
            Object arg = args[0];
            initTime("updateTime",  arg);
            initTime("updatedTime",  arg);
        }
    }
}
