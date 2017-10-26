
package org.unicorn.framework.core.aop;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.base.UnicornContext;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class LogAspect extends AbstractService {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controllerPointCut() {
	}
	
	

	@Before("controllerPointCut()") //
	public void before(JoinPoint  pjp) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		long beginTime = System.currentTimeMillis();
		UnicornContext.setValue("beginTime", beginTime);
		MethodSignature signature = (MethodSignature) pjp.getSignature();
        String url=request.getRequestURL().toString();
		info("请求开始。。。。。");
		info("请求地址:{}", url );
		
		// 记录下请求内容
		info("请求IP : {} ", request.getRemoteAddr());
		info("请求方法名 : {}",signature.getDeclaringTypeName() + "." + signature.getName());
		Object args[]=pjp.getArgs();
		for(Object arg:args){
			if(arg instanceof Serializable){
				info("请求报文 :{} ", new Gson().toJson(arg));
			}
		}
	}
	@AfterReturning(pointcut="controllerPointCut()",returning = "ret") //
	public void afterReturning(Object ret) {
		info("请求响应报文：{}", new Gson().toJson(ret));
		long costMs = System.currentTimeMillis() - Long.valueOf(UnicornContext.getValue("beginTime").toString());
		info("请求结束，耗时：{}ms", costMs);
	}

}
