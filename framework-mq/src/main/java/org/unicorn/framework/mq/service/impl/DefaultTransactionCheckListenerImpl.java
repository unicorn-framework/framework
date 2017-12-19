package org.unicorn.framework.mq.service.impl;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 未决事务，服务器回查客户端，broker端发起请求代码没有被调用，所以此处代码可能没用
 * @author xiebin
 *
 */
public class DefaultTransactionCheckListenerImpl implements TransactionCheckListener {

	@Override
	public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
		return LocalTransactionState.COMMIT_MESSAGE;
	}
}