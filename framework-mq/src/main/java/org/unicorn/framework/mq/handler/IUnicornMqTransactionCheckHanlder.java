package org.unicorn.framework.mq.handler;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import sun.security.util.PendingException;

/**
 * 本地事务反查处理器：由事务消息发起方实现
 * @author
 */
public interface IUnicornMqTransactionCheckHanlder {
    /**
     *
     * @param messageExt
     * @return
     * @throws PendingException
     */
    boolean supports(MessageExt messageExt) throws PendingException;

    /**
     *
     * @param messageExt
     * @return
     * @throws PendingException
     */
    LocalTransactionState execute(MessageExt messageExt) throws PendingException;

    /**
     * Bean的顺序
     *
     * @return
     * @throws PendingException
     */
    default Integer order() throws PendingException {
        return 0;
    }
}
