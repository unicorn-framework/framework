
package org.unicorn.framework.mq.handler;

import org.apache.rocketmq.common.message.MessageExt;
import org.unicorn.framework.base.base.SpringContextHolder;
import sun.security.util.PendingException;

import java.util.Map;

/**
 * 处理适配器
 *
 * @author xiebin
 */
public class UnicornMqTransactionHandlerAdapter {
    /**
     * @param messageExt
     * @return
     * @throws PendingException
     */
    public static IUnicornMqTransactionCheckHanlder getHandler(MessageExt messageExt) throws PendingException {
        Map<String, IUnicornMqTransactionCheckHanlder> beanMaps = SpringContextHolder.getApplicationContext().getBeansOfType(IUnicornMqTransactionCheckHanlder.class);
        for (String beanName : beanMaps.keySet()) {
            IUnicornMqTransactionCheckHanlder handler = beanMaps.get(beanName);
            try {
                if (handler.supports(messageExt)) {
                    return handler;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new DefaultUnicornMqTransactionCheckHanlder();
    }
}
