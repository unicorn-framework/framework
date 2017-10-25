
package org.unicorn.framework.mq.intercapter;

import java.io.Serializable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.base.annotation.JmsCapAnnotation;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.mq.domain.DestinactionDomain;
import org.unicorn.framework.mq.service.IMessageProduerService;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class JmsIntereptor extends AbstractService {
   @Autowired
   private IMessageProduerService messageProduerService;
   
//	@Pointcut("@annotation()")
//	public void jmsCapPointCut() {
//	}
	
	@Pointcut("@annotation(org.unicorn.framework.base.annotation.JmsCapAnnotation)")
	public void controllerPointCut() {
	}
	

	@Before("controllerPointCut()") //
	public void before(JoinPoint  pjp) {
		Object args[]=pjp.getArgs();
		for(Object arg:args){
			if(arg instanceof Serializable){
				info("请求报文 :{} ", new Gson().toJson(arg));
			}
		}
	}
	@AfterReturning(pointcut="controllerPointCut()",returning = "ret") //
	public void afterReturning(JoinPoint  pjp,Object ret) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		JmsCapAnnotation jmsCapAnnation=signature.getMethod().getAnnotation(JmsCapAnnotation.class);
		if(jmsCapAnnation==null){
			return;
		}
		if(!(ret  instanceof ResponseDto<?>)){
			error("异常");
		}
		ResponseDto<?> dto=(ResponseDto<?>)ret;
		if(dto.isSuccess()){
			try {
				messageProduerService.sendMessage(DestinactionDomain.builder()
						.destinactionName(jmsCapAnnation.destination())
						.jmsCommunicationType(jmsCapAnnation.jmsCommunicationType())
						.build(), "拦截器发送消息");
			} catch (PendingException e) {
				error("消息发送失败",e);
			}
		}else{
			
		}
	}

}
