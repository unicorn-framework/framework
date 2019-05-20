package org.unicorn.framework.transaction.config;

import com.alibaba.fescar.core.context.RootContext;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

import java.util.concurrent.Callable;

/**
 * @author  xiebin
 * Fescar hysrixConcurrentStratepy
 * Fescar hystrix 默认传递XID的方式是基于
 */
public class FescarHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
    private HystrixConcurrencyStrategy delegate;

    public FescarHystrixConcurrencyStrategy() {
        this.delegate = HystrixPlugins.getInstance().getConcurrencyStrategy();
        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(this);
    }

    @Override
    public <K> Callable<K> wrapCallable(Callable<K> c) {
        if (c instanceof FescarContextCallable) {
            return c;
        }

        Callable<K> wrappedCallable;
        if (this.delegate != null) {
            wrappedCallable = this.delegate.wrapCallable(c);
        }
        else {
            wrappedCallable = c;
        }
        if (wrappedCallable instanceof FescarContextCallable) {
            return wrappedCallable;
        }

        return new FescarContextCallable<>(wrappedCallable);
    }

    private static class FescarContextCallable<K> implements Callable<K> {

        private final Callable<K> actual;
        private final String xid;

        FescarContextCallable(Callable<K> actual) {
            this.actual = actual;
            this.xid = RootContext.getXID();
        }

        @Override
        public K call() throws Exception {
            try {
                RootContext.bind(xid);
                return actual.call();
            }
            finally {
                RootContext.unbind();
            }
        }

    }
}
