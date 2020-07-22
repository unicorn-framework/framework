
package org.unicorn.framework.web.handler;

import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.web.base.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *处理适配器
 * @author xiebin
 *
 */
public class HandlerAdapter  {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <S> IHandler<S> getHandler(S s) throws PendingException{
		Map<String, IHandler> beanMaps= SpringContextHolder.getApplicationContext().getBeansOfType(IHandler.class);
		for(String beanName:beanMaps.keySet()){
			IHandler<S> handler=beanMaps.get(beanName);
			try{
				if(handler.supports(s)){
					return handler;
				}
			}catch(Exception e){

			}

		}
		throw new PendingException(SysCode.NOT_FOUND_HANDLER,"没有找到对应的处理类");
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <S>  List<IHandler<S>> handlerList(S s) throws PendingException{
		List<IHandler<S>> list=new ArrayList<>();
		Map<String, IHandler> beanMaps=SpringContextHolder.getApplicationContext().getBeansOfType(IHandler.class);
		for(String beanName:beanMaps.keySet()){
			IHandler<S> handler=beanMaps.get(beanName);
			try{
				if(handler.supports(s)){
					list.add(handler);
				}
			}catch(Exception e){

			}
		}
		return list;
	}
}
