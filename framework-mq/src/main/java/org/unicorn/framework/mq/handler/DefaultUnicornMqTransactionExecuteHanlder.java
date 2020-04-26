package org.unicorn.framework.mq.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import sun.security.util.PendingException;

/**
 * 本地事务处理器====没有其它实现类的时候默认实现
 *
 * @author xiebin
 */
@Slf4j
public class DefaultUnicornMqTransactionExecuteHanlder implements IUnicornMqTransactionExecuteHanlder {
    @Override
    public boolean supports(Message message,Object msgObj) throws PendingException {
        return false;
    }

    @Override
    public LocalTransactionState execute(Message message,Object msgObj) throws PendingException {
        log.info("本地事务处理器====》系统默认实现");
        return LocalTransactionState.COMMIT_MESSAGE;
    }


}
