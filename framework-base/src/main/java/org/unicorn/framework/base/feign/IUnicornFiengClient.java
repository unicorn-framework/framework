package org.unicorn.framework.base.feign;

/**
 * @author xiebin
 */
public interface IUnicornFiengClient {
    /**
     * 是否支持当前 fallback factory
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> boolean support(Class<T> clazz);
}
