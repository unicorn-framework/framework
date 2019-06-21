package org.unicorn.framework.base.feign;

public interface IUnicornFiengClient {
    public <T> boolean support(Class<T> clazz);

}
