
package org.unicorn.framework.core.intercepter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.base.UnicornContext;
import org.unicorn.framework.base.annotation.EventualConsistencyAnnotation;
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
public class EventualConsistencyIntereptor extends AbstractService {
   @Value("${message.center.domain:http://localhost:8080}")
   private String messageCenterDomain;
   
   private Gson gson=new Gson();
	
	@Pointcut("@annotation(org.unicorn.framework.base.annotation.EventualConsistencyAnnotation)")
	public void eventualConsistencyPointCut() {
	}
    /**
     * 方法调用之前 调用消息服务生成待发消息记录
     * @param pjp
     */
	@SuppressWarnings("unchecked")
	@Before("eventualConsistencyPointCut()") //
	public void before(JoinPoint  pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		EventualConsistencyAnnotation jmsCapAnnation=signature.getMethod().getAnnotation(EventualConsistencyAnnotation.class);
		if(jmsCapAnnation==null){
			return;
		}
		try {
			//预保存消息
			Map<String,Object> messageMap=new HashMap<>();
			//设置消息通讯类型            
			messageMap.put("jmsCommunicationType", jmsCapAnnation.jmsCommunicationType().getCode());
			//设置消息发送的目的地名称
			messageMap.put("destinactionName", jmsCapAnnation.destination());
			//设置消息初始状态 0:待发送 1: 已发送 2:已完成
			messageMap.put("status", 0);
			//设置消息体
			messageMap.put("messageBody", getMessageBody(pjp));
			
			String messageInfoStr=CoreHttpUtils.post(messageCenterDomain+"/message/save",messageMap);
			ResponseDto<Map<String,Object>> resDto=gson.fromJson(messageInfoStr, ResponseDto.class);
			UnicornContext.setValue("messageInfo", resDto.getData());
		} catch (Exception e) {
			error("消息预保存失败",e);
			
		}
	}
	
	/**
	 * 获取消息体
	 * @param pjp
	 * @return
	 */
	private String getMessageBody(JoinPoint  pjp ){
		Object args[]=pjp.getArgs();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		String paramtersNames[]=signature.getParameterNames();
		Map<String,Object> paramMap=new HashMap<>();
		for(int i=0;i<args.length;i++){
			if(args[i] instanceof Serializable){
				paramMap.put(paramtersNames[i], args[i]);
			}
		}
		return gson.toJson(paramMap);
	}
	
	/**
	 * 方法执行之后 处理消息状态
	 * @param pjp
	 * @param ret
	 */
	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut="eventualConsistencyPointCut()",returning = "ret") 
	public void afterReturning(JoinPoint  pjp,Object ret) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		EventualConsistencyAnnotation jmsCapAnnation=signature.getMethod().getAnnotation(EventualConsistencyAnnotation.class);
		if(jmsCapAnnation==null){
			return;
		}
		ResponseDto<?> dto=(ResponseDto<?>)ret;
		Map<String,Object> messageMap= (Map<String, Object>) UnicornContext.getValue("messageInfo");
		//方法执行成功
		if(dto.isSuccess()){
			try {
				//发送消息
				messageMap.put("jmsCommunicationType", jmsCapAnnation.jmsCommunicationType());
				Map<String,Object> messageBody =gson.fromJson(messageMap.get("messageBody").toString(),Map.class);
				messageBody.put("messageId", messageMap.get("id"));
				messageMap.put("messageBody",gson.toJson(messageBody ));
				CoreHttpUtils.post(messageCenterDomain+"/message/send",messageMap);
				//修改消息状态为已发送
				Map<String,Object> queryMessageMap= (Map<String, Object>) UnicornContext.getValue("messageInfo");
				//设置消息状态为已发送
				Map<String,Object> updateMap=new HashMap<>();
				updateMap.put("status", 1);
				updateMap.put("id", queryMessageMap.get("id"));
				CoreHttpUtils.post(messageCenterDomain+"/message/update",updateMap);
			} catch (Exception e) {
				error("消息发送失败",e);
			}
		}else{
			try{
				Map<String,Object> deleteMap=new HashMap<>();
				deleteMap.put("id", messageMap.get("id"));
				CoreHttpUtils.post(messageCenterDomain+"/message/delete",deleteMap);
			}catch(Exception e){
				error("删除消息失败",e);
			}
			
		}
	}
	@SuppressWarnings("unchecked")
	@AfterThrowing("eventualConsistencyPointCut()") //
	public void throwException(JoinPoint  pjp) {
		Map<String,Object> messageMap= (Map<String, Object>) UnicornContext.getValue("messageInfo");
		try{
			Map<String,Object> deleteMap=new HashMap<>();
			deleteMap.put("id", messageMap.get("id"));
			CoreHttpUtils.post(messageCenterDomain+"/message/delete",deleteMap);
		}catch(Exception e){
			error("删除消息失败",e);
		}
	}
}
