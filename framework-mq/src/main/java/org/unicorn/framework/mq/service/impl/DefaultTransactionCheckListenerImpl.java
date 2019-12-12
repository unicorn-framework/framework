package org.unicorn.framework.mq.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.unicorn.framework.mq.handler.UnicornMqTransactionHandlerAdapter;


/**
 * 未决事务，服务器回查客户端，当发生网络抖动，导致确认消息没有发成功的场景下，会进行轮询反查本地事务状态
 *
 * @author xiebin
 */
@Slf4j
public class DefaultTransactionCheckListenerImpl implements TransactionCheckListener {

    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        log.info("topic==>" + msg.getTopic() + "tag==>" + msg.getTags() + "body==>" + new String(msg.getBody()));
        return UnicornMqTransactionHandlerAdapter.getHandler(msg).execute(msg);

    }
}