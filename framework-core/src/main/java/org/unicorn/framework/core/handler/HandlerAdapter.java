
package org.unicorn.framework.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	
	public static <T> List<T> getHandler(Class<T> clazz) throws PendingException{
		List<T> list=new ArrayList<>();
		Map<String, T> beanMaps=SpringContextHolder.getApplicationContext().getBeansOfType(clazz);
		if(beanMaps==null||beanMaps.size()==0){
			return list;
		}
		Set<String> beanNameSet=beanMaps.keySet();
		for(String beanName:beanNameSet){
			list.add(beanMaps.get(beanName));
		}
		return null;
	}
	
}
