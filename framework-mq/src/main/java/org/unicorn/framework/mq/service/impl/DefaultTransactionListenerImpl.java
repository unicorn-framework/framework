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
public class DefaultTransactionListenerImpl implements TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("topic==>" + message.getTopic() + "tag==>" + message.getTags() + "body==>" + new String(message.getBody()));
        log.info(new Gson().toJson(o));
        return UnicornMqTransactionExecuteHandlerAdapter.getHandler(message).execute(message);
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("topic==>" + messageExt.getTopic() + "tag==>" + messageExt.getTags() + "body==>" + new String(messageExt.getBody()));
        return UnicornMqTransactionExecuteHandlerAdapter.getHandler(messageExt).execute(messageExt);
    }
}