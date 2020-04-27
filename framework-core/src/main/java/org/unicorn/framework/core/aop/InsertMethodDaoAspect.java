
package org.unicorn.framework.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.exception.PendingException;

import java.util.List;

/**
 * @author xiebin
 */
@Aspect
@Component
@Slf4j
public class InsertMethodDaoAspect extends DateforDaoBaseAspect {
    @Pointcut("execution(* *..dao..*.insert*(..))")
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
            if (arg instanceof List) {
                List<Object> boList = (List<Object>) arg;
                for (Object bo : boList) {
                    initTime("updateTime", bo);
                    initTime("updatedTime", bo);
                    initTime("createTime", bo);
                    initTime("createdTime", bo);
                }
            } else {
                initTime("updateTime", arg);
                initTime("updatedTime", arg);
                initTime("createTime", arg);
                initTime("createdTime", arg);
            }
        }
    }


}
