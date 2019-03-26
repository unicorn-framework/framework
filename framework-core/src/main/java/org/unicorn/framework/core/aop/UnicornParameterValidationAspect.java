
package org.unicorn.framework.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.core.dto.AbstractRequestDto;
import org.unicorn.framework.core.exception.PendingException;

/**
 *  接口参数自动校验
 * @author xiebin
 *
 */
@Aspect
@Component
public class UnicornParameterValidationAspect extends AbstractService {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controllerPointCut() {
	}
	
	

	@Before("controllerPointCut()")
	public void before(JoinPoint  pjp) throws PendingException {
		//获取所有参数数组
		Object args[]=pjp.getArgs();
		for(Object arg:args){
			if(arg instanceof AbstractRequestDto){
				AbstractRequestDto requestDto=(AbstractRequestDto) arg;
				requestDto.vaildatioinThrowException();
			}
		}
	}
}
