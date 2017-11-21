
package org.unicorn.framework.core.handler;

import java.util.Map;

import org.unicorn.framework.base.SpringContextHolder;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

/**
 *处理适配器
 * @author xiebin
 *
 */
public class HandlerAdapter  {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <S>  IHandler<S> getHandler(S s) throws PendingException{
		Map<String, IHandler> beanMaps=SpringContextHolder.getApplicationContext().getBeansOfType(IHandler.class);
		for(String beanName:beanMaps.keySet()){
			IHandler<S> handler=beanMaps.get(beanName);
			if(handler.supports(s)){
				return handler;
			}
		}
		throw new PendingException(SysCode.NOT_FOUND_HANDLER,"没有找到对应的处理类");
	}

}
