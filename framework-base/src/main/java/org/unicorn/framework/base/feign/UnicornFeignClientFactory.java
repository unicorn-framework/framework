package org.unicorn.framework.base.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.base.base.UnicornContext;
import org.unicorn.framework.base.constants.UnicornConstants;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * feign客户端接口工厂类
 *
 * @author xiebin
 */
@Slf4j
public class UnicornFeignClientFactory {

    private static final Map<String, Object> feignClientProxtMap = new HashMap<>();

    private static Class<?> responseDtoClazz;

    private static Class<?> resBeanClazz;

    /**
     * @return
     */
    public static <T> T getFeignClientInstance(Class<T> clazz) {
        Throwable e = UnicornContext.getValue(UnicornConstants.FEIGN_THROWABLE);
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
        log.warn("没有发现["+clazz.getName()+"]的服务降级处理类");
        if (feignClientProxtMap.containsKey(clazz.getName())) {
            return (T) feignClientProxtMap.get(clazz.getName());
        }
        T t = null;
        if (clazz.isInterface()) {
            t = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FeignClientInvocationHandler(e));
        }

        if (t == null) {
            // 创建 cglib 代理类
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(clazz);
            enhancer.setCallback(new FeignClientInvocationHandler(e));
            t = (T) enhancer.create();
        }

        feignClientProxtMap.put(clazz.getName(), t);

        return t;

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
            throw new IllegalStateException("没有发现[ " + responseDtoclassName + " ]或 [ " + resBeanclassName + "]的依赖",e);
        }
    }


    static class FeignClientInvocationHandler implements InvocationHandler, MethodInterceptor {

        private Throwable e;

        public FeignClientInvocationHandler() {
        }

        public FeignClientInvocationHandler(Throwable e) {
            this.e = e;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return defaultMessage();
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return defaultMessage();
        }

        public Object defaultMessage() {
            try {
                Constructor<?> redBeanConstr = resBeanClazz.getConstructor(String.class, String.class);
                // 99000", "请稍后重试
                Object resBean = BeanUtils.instantiateClass(redBeanConstr, "99000", "请稍后重试");
                if (this.e == null) {
                    Constructor<?> dtoContru = responseDtoClazz.getConstructor(resBeanClazz);
                    return BeanUtils.instantiateClass(dtoContru, resBean);
                }
                Constructor<?> dtoContru = responseDtoClazz.getConstructor(resBeanClazz,String.class);
                return BeanUtils.instantiateClass(dtoContru, resBean,this.e.getMessage());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("没有发现[ " + resBeanClazz + " ]或 [ " + responseDtoClazz + "]的构造方法，ResponseDto的 构造参数类型为(String.class, String.class)",e);
            }
        }
    }
}
