package org.unicorn.framework.mq.trace.dispatch;

/**
 *  @author xiebin
 * 数据编码和发送模块
 */
public abstract class AsyncAppender {
    /**
     *编码数据上下文到缓冲区
     * @param context
     */
    public abstract void append(Object context);

    /**
     * 实际写数据操作
     */
    public abstract void flush();
}
