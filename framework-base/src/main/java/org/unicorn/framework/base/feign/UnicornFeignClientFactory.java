package org.unicorn.framework.base.feign;

import org.unicorn.framework.base.base.SpringContextHolder;

import java.util.Map;
import java.util.Set;

/**
 * feign客户端接口工厂类
 *
 * @author xiebin
 */
public class UnicornFeignClientFactory {
    /**
     * @return
     */
    public static <T> T getFeignClientInstance(Class<T> clazz) {
        //获取上下文中IUnicornFiengClient类型的对象
        Map<String, T> beanMaps = SpringContextHolder.getApplicationContext().getBeansOfType(clazz);
        Set<String> beanNameSet = beanMaps.keySet();
        for (String beanName : beanNameSet) {
            T t = beanMaps.get(beanName);
            IUnicornFiengClient iUnicornFiengClient=(IUnicornFiengClient)t;
            //判断当前对象是否支持  支持则iUnicornFiengClient返回目标对象
            if (iUnicornFiengClient.support(clazz)) {
                return (T) iUnicornFiengClient;
            }
        }

        return null;
    }
}
