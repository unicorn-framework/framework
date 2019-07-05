package org.unicorn.framework.base.feign;

import feign.hystrix.FallbackFactory;
import org.unicorn.framework.base.base.UnicornContext;
import org.unicorn.framework.base.constants.UnicornConstants;

/**
 * @param <T>
 * @author xiebin
 */
public interface IUnicornFallbackFactory<T> extends FallbackFactory<T> {
    @Override
    default T create(Throwable throwable) {
        UnicornContext.setValue(UnicornConstants.FEIGN_THROWABLE, throwable);
        return doCreate(throwable);
    }

    T doCreate(Throwable throwable);
}
