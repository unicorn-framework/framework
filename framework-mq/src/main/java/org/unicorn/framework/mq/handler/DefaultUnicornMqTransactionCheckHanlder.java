package org.unicorn.framework.mq.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import sun.security.util.PendingException;

/**
 * 默认实现，如果没有找到对应的处理类的情况下，默认本地事务成功
 *
 * @author xiebin
 */
@Slf4j
public class DefaultUnicornMqTransactionCheckHanlder implements IUnicornMqTransactionCheckHanlder {
    @Override
    public boolean supports(MessageExt messageExt) throws PendingException {
        return true;
    }

    @Override
    public LocalTransactionState execute(MessageExt messageExt) throws PendingException {
        log.info("本地事务状态查询器====》系统默认实现");
        return LocalTransactionState.COMMIT_MESSAGE;
    }


}
