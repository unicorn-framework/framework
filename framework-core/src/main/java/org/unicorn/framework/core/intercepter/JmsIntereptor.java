
package org.unicorn.framework.core.intercepter;

import java.io.Serializable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.base.UnicornContext;
import org.unicorn.framework.base.annotation.JmsCapAnnotation;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.domain.MessageInfo;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.util.http.CoreHttpUtils;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class JmsIntereptor extends AbstractService {
   @Value("${message.center.domain:http://localhost:8080}")
   private String messageCenterDomain;
	
	@Pointcut("@annotation(org.unicorn.framework.base.annotation.JmsCapAnnotation)")
	public void jmsCapPointCut() {
	}

	@Before("jmsCapPointCut()") //
	public void before(JoinPoint  pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		JmsCapAnnotation jmsCapAnnation=signature.getMethod().getAnnotation(JmsCapAnnotation.class);
		if(jmsCapAnnation==null){
			return;
		}
		try {
			MessageInfo messageInfo=new MessageInfo()  ;
			messageInfo.setDestinactionName(jmsCapAnnation.destination());
			messageInfo.setJmsCommunicationType(jmsCapAnnation.jmsCommunicationType());
			messageInfo.setMessageBody("test");
			UnicornContext.setValue("messageInfo", messageInfo);
			CoreHttpUtils.post(messageCenterDomain+"/message/save",messageInfo);
		} catch (Exception e) {
			error("消息发送失败",e);
			
		}
	}
	@AfterReturning(pointcut="jmsCapPointCut()",returning = "ret") //
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
				MessageInfo messageInfo=(MessageInfo) UnicornContext.getValue("messageInfo");
				CoreHttpUtils.post(messageCenterDomain+"/message/send",messageInfo);
			} catch (Exception e) {
				error("消息发送失败",e);
			}
		}
	}

}
