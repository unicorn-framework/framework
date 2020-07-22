package org.unicorn.framework.web.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.unicorn.framework.base.base.UnicornContext;
import org.unicorn.framework.base.constants.UnicornConstants;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * feign客户端接口工厂类
 * 用户返回fegin接口对应的降级类
 *
 * @author xiebin
 */
@Slf4j
public class UnicornFeignClientFactory {

    private static final Map<String, Object> feignClientProxtMap = new ConcurrentHashMap<>();

    private static Class<?> responseDtoClazz;

    private static Class<?> resBeanClazz;

    /**
     * @return
     */
    public static <T> T getFeignClientInstance(Class<T> clazz, Throwable throwable) {
        //获取上下文中IUnicornFiengClient类型的对象
        Map<String, T> beanMaps = SpringContextHolder.getApplicationContext().getBeansOfType(clazz);
        Set<String> beanNameSet = beanMaps.keySet();
        for (String beanName : beanNameSet) {
            T t = beanMaps.get(beanName);
            if (t instanceof IUnicornFiengClient) {
                IUnicornFiengClient iUnicornFiengClient = (IUnicornFiengClient) t;
                //判断当前对象是否支持  支持则iUnicornFiengClient返回目标对象
                if (iUnicornFiengClient.support(clazz)) {
                    return (T) iUnicornFiengClient;
                }
            }
        }
        log.warn("没有发现[" + clazz.getName() + "]的客户端自定义服务降级处理类");
        if (feignClientProxtMap.containsKey(clazz.getName())) {
            return (T) feignClientProxtMap.get(clazz.getName());
        }
        T t = getClientFallbackProxy(clazz, throwable);
        feignClientProxtMap.put(clazz.getName(), t);
        return t;
    }


    /**
     * @return
     */
    public static <T> T getFeignClientInstance(Class<T> clazz) {
        return getFeignClientInstance(clazz, null);
    }


    /**
     * 获取 fallback动态代理对象
     *
     * @param clazz
     * @param throwable
     * @param <T>
     * @return
     */
    private static <T> T getClientFallbackProxy(Class<T> clazz, Throwable throwable) {
        if (clazz.isInterface()) {
            log.info("通过JDK动态代理实例化[" + clazz.getName() + "]的服务降级处理类");
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FeignClientInvocationHandler(clazz, throwable));
        }
        log.info("通过CGLIB动态代理实例化[" + clazz.getName() + "]的服务降级处理类");
        // 创建 cglib 代理类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new FeignClientInvocationHandler(clazz, throwable));
        return (T) enhancer.create();

    }

    public static void clear() {
        feignClientProxtMap.clear();
    }

    static {
        String responseDtoclassName = "org.unicorn.framework.core.ResponseDto";
        String resBeanclassName = "org.unicorn.framework.core.ResBean";
        try {
            responseDtoClazz = Class.forName(responseDtoclassName);
            resBeanClazz = Class.forName(resBeanclassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("没有发现[ " + responseDtoclassName + " ]或 [ " + resBeanclassName + "]的依赖", e);
        }
    }


    static class FeignClientInvocationHandler implements InvocationHandler, MethodInterceptor {

        private Class clazz;
        private Throwable throwable;

        //        public FeignClientInvocationHandler() {
//        }

        public FeignClientInvocationHandler(Class clazz, Throwable throwable) {
            this.clazz = clazz;
            this.throwable = throwable;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return defaultMessage();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return defaultMessage();
        }

        /**
         * 框架默认生成的降级处理逻辑
         *
         * @return
         */
        public Object defaultMessage() {
            if (throwable == null) {
                throwable = UnicornContext.getValue(UnicornConstants.FEIGN_THROWABLE);
            }
            log.error("接口降级:{}" + clazz.getName(), throwable);
            try {
                Constructor<?> redBeanConstr = resBeanClazz.getConstructor(String.class, String.class);
                // 99000", "请稍后重试
                Object resBean = BeanUtils.instantiateClass(redBeanConstr, "99000", "请稍后重试");
                Constructor<?> dtoContru = responseDtoClazz.getConstructor(resBeanClazz);
                return BeanUtils.instantiateClass(dtoContru, resBean);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("没有发现[ " + resBeanClazz + " ]或 [ " + responseDtoClazz + "]的构造方法，ResponseDto的 构造参数类型为(String.class, String.class)", e);
            }
        }
    }
}
