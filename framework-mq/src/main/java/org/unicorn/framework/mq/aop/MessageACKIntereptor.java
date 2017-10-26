
package org.unicorn.framework.mq.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.util.http.CoreHttpUtils;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class MessageACKIntereptor extends AbstractService {
   @Value("${message.center.domain:http://localhost:8080}")
   private String messageCenterDomain;
   
   private Gson gson=new Gson();
	
	@Pointcut("@annotation(org.springframework.jms.annotation.JmsListener)")
	public void messageAckPointCut() {
	}
    
	/**
	 * 方法执行之后 处理消息应答
	 * @param pjp
	 * @param ret
	 */
	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut="messageAckPointCut()",returning = "ret") 
	public void afterReturning(JoinPoint  pjp,Object ret) {
		try {
			Object args[]=pjp.getArgs();
			System.out.println(args[0]);
			Map<String,Object> messageBodyMap=gson.fromJson(args[0].toString(), Map.class);
			//设置消息状态为已发送
			Map<String,Object> updateMap=new HashMap<>();
			updateMap.put("status", 2);
			updateMap.put("id", messageBodyMap.get("messageId"));
			
			CoreHttpUtils.post(messageCenterDomain+"/message/update",updateMap);
		} catch (Exception e) {
			error("消息应答",e);
		}
	}
}
