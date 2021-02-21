
package org.unicorn.framework.gateway.handler;

import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理适配器
 *
 * @author xiebin
 */
public class GatewayHandlerAdapter {


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <S> IGatewayHandler<S> getHandler(S s) throws PendingException {
        Map<String, IGatewayHandler> beanMaps = GatewaySpringContextHolder.getApplicationContext().getBeansOfType(IGatewayHandler.class);
        for (String beanName : beanMaps.keySet()) {
            IGatewayHandler<S> handler = beanMaps.get(beanName);
            try {
                if (handler.supports(s)) {
                    return handler;
                }
            } catch (Exception e) {

            }

        }
        throw new PendingException(SysCode.NOT_FOUND_HANDLER, "没有找到对应的处理类");
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <S> List<IGatewayHandler<S>> handlerList(S s) throws PendingException {
        List<IGatewayHandler<S>> list = new ArrayList<>();
        Map<String, IGatewayHandler> beanMaps = SpringContextHolder.getApplicationContext().getBeansOfType(IGatewayHandler.class);
        for (String beanName : beanMaps.keySet()) {
            IGatewayHandler<S> handler = beanMaps.get(beanName);
            try {
                if (handler.supports(s)) {
                    list.add(handler);
                }
            } catch (Exception e) {

            }
        }
        return list;
    }
}
