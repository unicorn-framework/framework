package org.unicorn.framework.base.feign;

/**
 * @author  xiebin
 */
public interface IUnicornFiengClient {

     <T> boolean support(Class<T> clazz);
}
