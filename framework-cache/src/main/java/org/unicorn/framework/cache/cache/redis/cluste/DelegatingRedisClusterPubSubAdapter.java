package org.unicorn.framework.cache.cache.redis.cluste;

import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.pubsub.RedisClusterPubSubAdapter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.unicorn.framework.base.thread.UnicornThreadFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * reids 集群发布订单key过期委托类
 * @author zhanghaibo
 * @since 2020/5/13
 */
@Slf4j
public class DelegatingRedisClusterPubSubAdapter extends RedisClusterPubSubAdapter {

    @Setter
    private ExecutorService executorService;

    @Setter
    private List<RedisClusterKeyExpiredHandler> redisClusterKeyExpiredHandlerList;

    public DelegatingRedisClusterPubSubAdapter(List<RedisClusterKeyExpiredHandler> redisClusterKeyExpiredHandlerList) {
        this.redisClusterKeyExpiredHandlerList = redisClusterKeyExpiredHandlerList;
        this.executorService = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue(),new UnicornThreadFactory("redis-cluster-expired-pool"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void message(RedisClusterNode node, Object channel, Object message) {
        if (CollectionUtils.isEmpty(redisClusterKeyExpiredHandlerList)) {
            return;
        }
        for (RedisClusterKeyExpiredHandler redisClusterKeyExpiredHandler : redisClusterKeyExpiredHandlerList) {
            if (message instanceof String) {
                String key = (String) message;
                if (redisClusterKeyExpiredHandler.match(key)) {
                    if (executorService != null) {
                        executorService.execute(()->redisClusterKeyExpiredHandler.handle(key));
                    }else {
                        redisClusterKeyExpiredHandler.handle(key);
                    }
                }
            }else {
                log.warn("redis过期message不是String类型");
                redisClusterKeyExpiredHandler.handle(node, channel, message);
            }
        }
    }

    public void close(){
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}
