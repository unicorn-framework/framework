package org.unicorn.framework.cache.conifg;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.base.condition.ConditionalOnPropertyEmpty;
import org.unicorn.framework.cache.cache.redis.cluste.DelegatingRedisClusterPubSubAdapter;
import org.unicorn.framework.cache.cache.redis.cluste.LettuceSubscriber;
import org.unicorn.framework.cache.cache.redis.cluste.RedisClusterKeyExpiredHandler;

import java.util.List;


/**
 * resdis 集群client配置
 * @author zhanghaibo
 * @since 2020/5/13
 */
@ConditionalOnPropertyEmpty("spring.redis.cluster.nodes")
@Configuration
public class RedisClusterConfiguration {


    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private Integer database;


    @Bean(destroyMethod = "shutdown")
    ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisClusterKeyExpiredHandler.class)
    public DelegatingRedisClusterPubSubAdapter delegatingRedisClusterPubSubAdapter(
            @Autowired List<RedisClusterKeyExpiredHandler> redisClusterKeyExpiredHandlerList) {

        return new DelegatingRedisClusterPubSubAdapter(redisClusterKeyExpiredHandlerList);
    }

    @Bean(destroyMethod = "shutdown")
    RedisClusterClient clusterClient(ClientResources clientResources) {
        String[] split = nodes.split(",");
        RedisURI redisURI = RedisURI.create("redis://"+split[0]);
        redisURI.setPassword(password);
        redisURI.setDatabase(database);
        return RedisClusterClient.create(clientResources, redisURI);
    }

    @Bean(destroyMethod = "close")
    StatefulRedisClusterConnection statefulRedisClusterConnection(RedisClusterClient clusterClient) {
        return clusterClient.connect();
    }

    @Bean
    @ConditionalOnBean({RedisClusterClient.class,DelegatingRedisClusterPubSubAdapter.class})
    LettuceSubscriber lettuceSubscriber(@Qualifier("clusterClient") RedisClusterClient clusterClient,
                                        DelegatingRedisClusterPubSubAdapter delegatingRedisClusterPubSubAdapter){
        LettuceSubscriber lettuceSubscriber = new LettuceSubscriber();
        lettuceSubscriber.setClusterClient(clusterClient);
        lettuceSubscriber.setDelegatingRedisClusterPubSubAdapter(delegatingRedisClusterPubSubAdapter);
        lettuceSubscriber.startListener();
        return lettuceSubscriber;
    }

}
