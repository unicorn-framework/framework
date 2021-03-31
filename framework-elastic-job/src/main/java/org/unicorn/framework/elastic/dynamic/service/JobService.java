package org.unicorn.framework.elastic.dynamic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.util.JsonUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiebin
 */
@Service
@Slf4j
public class JobService {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;

    @Autowired
    private ApplicationContext ctx;

    @Autowired(required = false)
    private IUnicornJobPersistenceService iUnicornJobPersistenceService;

    /**
     * 添加job总数
     */
    private Map<String, AtomicInteger> JOB_ADD_COUNT = new ConcurrentHashMap<>();


    /**
     * 增加动态job
     *
     * @param job
     */
    public void addJob(Job job) {
        //初始化job
        addJob(job, null);
        //动态job 持久化
        persistenceJob(job);
    }

    /**
     * 动态job 持久化
     *
     * @param job
     */
    private void persistenceJob(Job job) {
        if (iUnicornJobPersistenceService == null) {
            return;
        }
        try {
            iUnicornJobPersistenceService.saveJob(job);
        } catch (Exception e) {
            log.error("job持久化失败:jobName=" + job.getJobName(), e);
        }
    }


    /**
     * 增加job
     *
     * @param job
     */
    public void addJob(Job job, ElasticJob elasticjob) {
        try {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
            if (elasticjob == null) {
                Class clazz = Class.forName(job.getJobClass());
                elasticjob = (ElasticJob) ctx.getBean(clazz);
            }
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ScheduleJobBootstrap.class);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(coordinatorRegistryCenter);
            factory.addConstructorArgValue(elasticjob);
            factory.addConstructorArgValue(jobCoreConfiguration(job));
            defaultListableBeanFactory.registerBeanDefinition(job.getJobName() + "UnicornJobScheduler", factory.getBeanDefinition());
            ScheduleJobBootstrap scheduleJobBootstrap = (ScheduleJobBootstrap) ctx.getBean(job.getJobName() + "UnicornJobScheduler");

            scheduleJobBootstrap.schedule();
            log.info("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit success");
        } catch (Exception e) {
            log.error("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit failure", e);
        }
    }

    /**
     * 根据job名称删除job
     *
     * @param jobName
     * @throws Exception
     */
    public void removeJob(String jobName) throws Exception {
        coordinatorRegistryCenter.remove("/" + jobName);
        //
        if (iUnicornJobPersistenceService == null) {
            return;
        }
        try {
            Job job = new Job();
            job.setJobName(jobName);
            iUnicornJobPersistenceService.removeJob(job);
        } catch (Exception e) {
            log.error("job删除失败:jobName=" + jobName, e);
        }
    }

    /**
     * 开启任务监听,当有任务添加时，监听zk中的数据增加，自动在其他节点也初始化该任务
     */
    public void monitorJobRegister() {
        ZookeeperRegistryCenter zookeeperRegistryCenter = (ZookeeperRegistryCenter) coordinatorRegistryCenter;
        CuratorFramework curatorFramework = zookeeperRegistryCenter.getClient();
        @SuppressWarnings("resource")
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, "/", true);
        PathChildrenCacheListener childrenCacheListener = (client, event) -> {
            ChildData data = event.getData();
            switch (event.getType()) {
                case CHILD_ADDED:
                    try {
                        String config = new String(client.getData().forPath(data.getPath() + "/config"));
                        Job job = JsonUtils.toBean(Job.class, config);
                        // 启动时任务会添加数据触发事件，这边需要去掉第一次的触发，不然在控制台进行手动触发任务会执行两次任务
                        if (!JOB_ADD_COUNT.containsKey(job.getJobName())) {
                            JOB_ADD_COUNT.put(job.getJobName(), new AtomicInteger());
                        }
                        int count = JOB_ADD_COUNT.get(job.getJobName()).incrementAndGet();
                        if (count > 1) {
                            addJob(job, null);
                        }
                    } catch (Exception e) {
                        zookeeperRegistryCenter.remove(data.getPath());
                        log.error("监听增加事件异常:{}", e.getMessage());
                    }
                    break;
                case CHILD_REMOVED:
                    log.info("节点删除==>" + data.getPath());
                    break;
                default:
                    break;
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        try {
            childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * jobCoreConfiguration 核心配置
     *
     * @param job
     * @return
     */
    public JobConfiguration jobCoreConfiguration(Job job) {
        return JobConfiguration.newBuilder(job.getJobName(), job.getShardingTotalCount())
                .shardingItemParameters(job.getShardingItemParameters())
                .description(job.getDescription())
                .failover(job.isFailover())
                .jobParameter(job.getJobParameter())
                .misfire(job.isMisfire())
                .cron(job.getCron())
                .build();
    }


    /**
     * 获取 elasticJob监听器
     *
     * @param job
     * @return
     */
    public List<BeanDefinition> getTargetElasticJobListeners(Job job) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = job.getListener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = job.getDistributedListener();
        long startedTimeoutMilliseconds = job.getStartedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = job.getCompletedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(startedTimeoutMilliseconds);
            factory.addConstructorArgValue(completedTimeoutMilliseconds);
            result.add(factory.getBeanDefinition());
        }
        return result;
    }


}
