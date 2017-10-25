
package org.unicorn.framework.core.intercepter;

import java.io.Serializable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.core.ResponseDto;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class JmsIntereptor extends AbstractService {

	@Pointcut("@annotation(org.unicorn.framework.annotion.JmsCapAnnation)")
	public void jmsCapPointCut() {
	}
	
	

	@Before("jmsCapPointCut()") //
	public void before(JoinPoint  pjp) {
		Object args[]=pjp.getArgs();
		for(Object arg:args){
			if(arg instanceof Serializable){
				info("请求报文 :{} ", new Gson().toJson(arg));
			}
		}
	}
	@AfterReturning(pointcut="jmsCapPointCut()",returning = "ret") //
	public void afterReturning(Object ret) {
		if(!(ret  instanceof ResponseDto<?>)){
			error("异常");
		}
		ResponseDto<?> dto=(ResponseDto<?>)ret;
		if(dto.isSuccess()){
			
		}else{
			
		}
	}

}
