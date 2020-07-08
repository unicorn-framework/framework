package org.unicorn.framework.cache.service;

import org.springframework.lang.Nullable;
import org.unicorn.framework.core.exception.PendingException;

/**
 * 缓存回调接口
 *
 * @param <T>
 * @author xiebin
 */
@FunctionalInterface
public interface UnicornCacheCallback<T> {
    @Nullable
    T doInCache() throws PendingException;
}


