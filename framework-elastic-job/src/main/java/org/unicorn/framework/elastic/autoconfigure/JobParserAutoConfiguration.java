package org.unicorn.framework.elastic.autoconfigure;

import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.shardingsphere.elasticjob.tracing.api.TracingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 任务自动配置
 *
 * @author xiebin
 */
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class JobParserAutoConfiguration {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Autowired
    @Qualifier("unicornDataSource")
    private DataSource dataSource;

    /**
     * 初始化Zookeeper注册中心
     *
     * @return
     */
    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter zookeeperRegistryCenter() {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());
        zkConfig.setBaseSleepTimeMilliseconds(zookeeperProperties.getBaseSleepTimeMilliseconds());
        zkConfig.setConnectionTimeoutMilliseconds(zookeeperProperties.getConnectionTimeoutMilliseconds());
        zkConfig.setDigest(zookeeperProperties.getDigest());
        zkConfig.setMaxRetries(zookeeperProperties.getMaxRetries());
        zkConfig.setMaxSleepTimeMilliseconds(zookeeperProperties.getMaxSleepTimeMilliseconds());
        zkConfig.setSessionTimeoutMilliseconds(zookeeperProperties.getSessionTimeoutMilliseconds());
        CoordinatorRegistryCenter coordinatorRegistryCenter = new ZookeeperRegistryCenter(zkConfig);
        coordinatorRegistryCenter.init();
        return coordinatorRegistryCenter;
    }

    /**
     * 定义日志数据库事件溯源配置
     *
     * @return
     */
    @ConditionalOnProperty(prefix = "unicorn.elastic.job.zk", value = "traceEnable", havingValue = "true")
    @Bean
    public TracingConfiguration tracingConfig() {
        return new TracingConfiguration<>("RDB", dataSource);
    }

}
