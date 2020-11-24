package org.unicorn.framework.util;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.util.function.Supplier;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-09-01 16:46
 */
public class CglibUtil {

    public static <T> T getProxyObject(Supplier<? extends MethodInterceptor> supplierCallback, T proxyedObj) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(proxyedObj.getClass());
        enhancer.setCallback(supplierCallback.get());
        return (T) enhancer.create();
    }


}
