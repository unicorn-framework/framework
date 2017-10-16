
package org.unicorn.framework.core.intercepter;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.AbstractService;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class LogIntereptor extends AbstractService {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controllerPointCut() {
	}

	@Around("controllerPointCut()") //
	public Object Interceptor(ProceedingJoinPoint pjp) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		long beginTime = System.currentTimeMillis();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
        String url=request.getRequestURL().toString();
		info("请求开始。。。。。");
		info("请求地址:{}", url );
		Object result = null;
		// 记录下请求内容
		info("请求IP : {} ", request.getRemoteAddr());
		info("请求方法名 : {}",signature.getDeclaringTypeName() + "." + signature.getName());
		info("请求报文 :{} " + new Gson().toJson(pjp.getArgs()));
		try {
			if (result == null) {
				// 一切正常的情况下，继续执行被拦截的方法
				result = pjp.proceed();
				
			}
		info("请求响应报文：{}", new Gson().toJson(result));
		} catch (Throwable e) {
			error("exception:",e);
		}
		long costMs = System.currentTimeMillis() - beginTime;
		info("请求结束，耗时：{}ms", costMs);
		
		return result;
	}

}
