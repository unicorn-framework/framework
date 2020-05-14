package org.unicorn.framework.cache.cache.redis.cluste;

import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.cluster.pubsub.api.async.NodeSelectionPubSubAsyncCommands;
import io.lettuce.core.cluster.pubsub.api.async.PubSubAsyncNodeSelection;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import lombok.Setter;


/**
 * redis发布订阅配置
 * @author zhanghaibo
 * @since 2020/5/14
 */
public class LettuceSubscriber extends RedisPubSubAdapter {

    private static final String EXPIRED_CHANNEL = "__keyevent@0__:expired";

    @Setter
    private RedisClusterClient clusterClient;

    @Setter
    private DelegatingRedisClusterPubSubAdapter delegatingRedisClusterPubSubAdapter;


    /**
     * 启动监听
     */

    public void startListener() {
        // 异步订阅
        StatefulRedisClusterPubSubConnection<String, String> pubSubConnection = clusterClient.connectPubSub();
        pubSubConnection.setNodeMessagePropagation(true);
        pubSubConnection.addListener(delegatingRedisClusterPubSubAdapter);

        PubSubAsyncNodeSelection<String, String> masters = pubSubConnection.async().masters();
        NodeSelectionPubSubAsyncCommands<String, String> commands = masters.commands();
        commands.subscribe(EXPIRED_CHANNEL);
    }
}
