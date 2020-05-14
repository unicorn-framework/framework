package org.unicorn.framework.cache.cache.redis.cluste;

import org.springframework.core.Ordered;

/**
 * redis集群key过期处理接口
 * @author zhanghaibo
 * @since 2020/5/13
 */
public interface RedisClusterKeyExpiredHandler extends Ordered {

    /**
     * key 是否匹配
     * @param key redis 过期的key
     * @return
     */
    boolean match(String key);

    /**
     * 过期处理的业务
     * @param key
     */
    void handle(String key);

    @Override
    default int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    }
}
