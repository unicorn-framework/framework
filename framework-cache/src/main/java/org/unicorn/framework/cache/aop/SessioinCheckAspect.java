
package org.unicorn.framework.cache.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.constants.Constants;
import org.unicorn.framework.core.exception.PendingException;

/**
 * 
 * @author xiebin
 *
 */
@Aspect
@Component
public class SessioinCheckAspect extends AbstractService {

	@Pointcut("@annotation(org.unicorn.framework.cache.annoation.SessionCheck)")
	public void controllerPointCut() {
	}
	
	

	@Before("controllerPointCut()") //
	public void before(JoinPoint  pjp) throws PendingException{
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session=request.getSession();
		Object sessionFlagObject=session.getAttribute(Constants.SESSION_FLAG);
        Boolean loginFlag=new Boolean(sessionFlagObject==null?null:sessionFlagObject.toString());
		if(!loginFlag){
			throw new PendingException(SysCode.SESSION_ERROR);
		}
	}
}
