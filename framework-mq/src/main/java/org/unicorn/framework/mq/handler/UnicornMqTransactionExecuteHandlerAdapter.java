
package org.unicorn.framework.mq.handler;

import org.apache.rocketmq.common.message.Message;
import org.unicorn.framework.base.base.SpringContextHolder;
import sun.security.util.PendingException;

import java.util.Map;

/**
 * 本地事务处理适配器
 *
 * @author xiebin
 */
public class UnicornMqTransactionExecuteHandlerAdapter {
    /**
     * @param message
     * @return
     * @throws PendingException
     */
    public static IUnicornMqTransactionExecuteHanlder getHandler(Message message) throws PendingException {
        Map<String, IUnicornMqTransactionExecuteHanlder> beanMaps = SpringContextHolder.getApplicationContext().getBeansOfType(IUnicornMqTransactionExecuteHanlder.class);
        for (String beanName : beanMaps.keySet()) {
            IUnicornMqTransactionExecuteHanlder handler = beanMaps.get(beanName);
            try {
                if (handler.supports(message)) {
                    return handler;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DefaultUnicornMqTransactionExecuteHanlder();
    }
}
