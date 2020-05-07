package org.unicorn.framework.mq.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.unicorn.framework.mq.handler.UnicornMqTransactionCheckHandlerAdapter;
import org.unicorn.framework.mq.handler.UnicornMqTransactionExecuteHandlerAdapter;


/**
 * @author xiebin
 */
@Slf4j
public class UnicornTransactionListenerImpl implements TransactionListener {
    /**
     * 本地事务执行器
     *
     * @param message
     * @param o
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("本地事务执行：topic==>" + message.getTopic() + "tag==>" + message.getTags() + "body==>" + new String(message.getBody()));
        return UnicornMqTransactionExecuteHandlerAdapter.getHandler(message, o).execute(message, o);
    }

    /**
     * 本地未知事务状态检查器
     *
     * @param messageExt
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("本地事务回查：topic==>" + messageExt.getTopic() + "tag==>" + messageExt.getTags() + "body==>" + new String(messageExt.getBody()));
        return UnicornMqTransactionCheckHandlerAdapter.getHandler(messageExt).execute(messageExt);
    }
}