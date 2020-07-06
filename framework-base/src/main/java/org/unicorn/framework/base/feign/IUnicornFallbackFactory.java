package org.unicorn.framework.base.feign;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unicorn.framework.base.base.UnicornContext;
import org.unicorn.framework.base.constants.UnicornConstants;

/**
 * feign fallback工厂接口类
 * 其他具体的fallback工厂实现该类
 *
 * @param <T>
 * @author xiebin
 */
public interface IUnicornFallbackFactory<T> extends FallbackFactory<T> {
    Logger log = LoggerFactory.getLogger("unicorn-fallback");

    @Override
    default T create(Throwable throwable) {
        //将trowable存储在上下文中  thread
        log.error("接口熔断:{}", throwable);
        UnicornContext.setValue(UnicornConstants.FEIGN_THROWABLE, throwable);
        return doCreate(throwable);
    }

    /**
     * 创建具体的降级类
     *
     * @param throwable
     * @return
     */
    T doCreate(Throwable throwable);
}
