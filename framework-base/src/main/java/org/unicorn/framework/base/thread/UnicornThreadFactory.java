package org.unicorn.framework.base.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程名字的线程工厂
 * @author zhanghaibo
 * @since 2020/5/13
 */
public class UnicornThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    private final String poolName;

    private final String threadName;

    public UnicornThreadFactory(String poolName, String threadName) {
        if (poolName == null|| poolName.length()==0) {
            throw new NullPointerException("poolName is not Null");
        }
        if (threadName == null) {
            threadName = "thread";
        }
        this.poolName = poolName;
        this.threadName = threadName;
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = poolName+"-" +
                poolNumber.getAndIncrement() +
                "-"+threadName+"-";
    }

    public UnicornThreadFactory(String poolName) {
        this(poolName, null);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
