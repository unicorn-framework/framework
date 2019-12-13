package org.unicorn.framework.mq.service.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.unicorn.framework.mq.handler.UnicornMqTransactionExecuteHandlerAdapter;


/**
 *
 *
 * @author xiebin
 */
@Slf4j
public class UnicornTransactionListenerImpl implements TransactionListener {
    /**
     * 本地事务执行器
     * @param message
     * @param o
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("topic==>" + message.getTopic() + "tag==>" + message.getTags() + "body==>" + new String(message.getBody()));
        return UnicornMqTransactionExecuteHandlerAdapter.getHandler(message).execute(message);
    }

    /**
     * 本地未知事务状态检查器
     * @param messageExt
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("topic==>" + messageExt.getTopic() + "tag==>" + messageExt.getTags() + "body==>" + new String(messageExt.getBody()));
        return UnicornMqTransactionExecuteHandlerAdapter.getHandler(messageExt).execute(messageExt);
    }
}