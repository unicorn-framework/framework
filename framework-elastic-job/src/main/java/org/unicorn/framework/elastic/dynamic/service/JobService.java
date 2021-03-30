package org.unicorn.framework.elastic.dynamic.service;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.elastic.contants.Contants;
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
public class JobService extends AbstractService {

    @Autowired
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Autowired
    private ApplicationContext ctx;

    @Autowired(required = false)
    private IUnicornJobPersistenceService iUnicornJobPersistenceService;

    /**
     * 添加job总数
     */
    private Map<String, AtomicInteger> JOB_ADD_COUNT = new ConcurrentHashMap<String, AtomicInteger>();

    /**
     * 增加job
     *
     * @param job
     */
    public void addJob(Job job) {
        // 核心配置
        BeanDefinitionBuilder factory = configuration(job);
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.registerBeanDefinition(job.getJobName() + "SpringJobScheduler", factory.getBeanDefinition());
        SpringJobScheduler springJobScheduler = (SpringJobScheduler) ctx.getBean(job.getJobName() + "SpringJobScheduler");
        springJobScheduler.init();
        info("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit success");
        //
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
     * 根据job名称删除job
     *
     * @param jobName
     * @throws Exception
     */
    public void removeJob(String jobName) throws Exception {
        zookeeperRegistryCenter.remove("/" + jobName);
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
                            addJob(job);
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

    public BeanDefinitionBuilder configuration(Job job) {
        // 核心配置
        JobCoreConfiguration coreConfig = jobCoreConfiguration(job);
        // 不同类型的任务配置处理
        JobTypeConfiguration typeConfig = jobTypeConfiguration(job, coreConfig);
        //Litejob核心配置
        LiteJobConfiguration jobConfig = liteJobConfig(job, typeConfig);
        // 构建SpringJobScheduler对象来初始化任务
        BeanDefinitionBuilder factory = createFactory(job, jobConfig);
        // 任务执行日志数据源，以名称获取
        addEventTraceRdbDataSource(factory, job);
        //job监听器
        addElasticJobListeners(factory, job);
        return factory;
    }

    /**
     * liteJobConfig 核心配置
     *
     * @param job
     * @param typeConfig
     * @return
     */
    public LiteJobConfiguration liteJobConfig(Job job, JobTypeConfiguration typeConfig) {
        return LiteJobConfiguration.newBuilder(typeConfig)
                .overwrite(job.isOverwrite())
                .disabled(job.isDisabled())
                .monitorPort(job.getMonitorPort())
                .monitorExecution(job.isMonitorExecution())
                .maxTimeDiffSeconds(job.getMaxTimeDiffSeconds())
                .jobShardingStrategyClass(job.getJobShardingStrategyClass())
                .reconcileIntervalMinutes(job.getReconcileIntervalMinutes())
                .build();
    }

    /**
     * jobCoreConfiguration 核心配置
     *
     * @param job
     * @return
     */
    public JobCoreConfiguration jobCoreConfiguration(Job job) {
        return JobCoreConfiguration.newBuilder(job.getJobName(), job.getCron(), job.getShardingTotalCount())
                .shardingItemParameters(job.getShardingItemParameters())
                .description(job.getDescription())
                .failover(job.isFailover())
                .jobParameter(job.getJobParameter())
                .misfire(job.isMisfire())
                .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), job.getJobProperties().getJobExceptionHandler())
                .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), job.getJobProperties().getExecutorServiceHandler())
                .build();
    }

    /**
     * 不同类型的任务配置处理
     *
     * @param job
     * @param coreConfig
     * @return
     */
    public JobTypeConfiguration jobTypeConfiguration(Job job, JobCoreConfiguration coreConfig) {
        JobTypeConfiguration typeConfig = null;
        String jobType = job.getJobType();
        //简单任务
        if (jobType.equals(Contants.SIMPLE_JOB)) {
            typeConfig = new SimpleJobConfiguration(coreConfig, job.getJobClass());
        }
        //流式任务
        if (jobType.equals(Contants.DATA_FLOW_JOB)) {
            typeConfig = new DataflowJobConfiguration(coreConfig, job.getJobClass(), job.isStreamingProcess());
        }
        //脚本任务
        if (jobType.equals(Contants.SCRIP_JOB)) {
            typeConfig = new ScriptJobConfiguration(coreConfig, job.getScriptCommandLine());
        }
        return typeConfig;
    }

    /**
     * 构建SpringJobScheduler对象来初始化任务
     *
     * @param job
     * @param jobConfig
     * @return
     */
    public BeanDefinitionBuilder createFactory(Job job, LiteJobConfiguration jobConfig) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
        factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        if (Contants.SCRIP_JOB.equals(job.getJobType())) {
            factory.addConstructorArgValue(null);
        } else {
            BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(job.getJobClass());
            factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
        }
        factory.addConstructorArgValue(zookeeperRegistryCenter);
        factory.addConstructorArgValue(jobConfig);
        return factory;
    }

    /**
     * 任务执行日志数据源，以名称获取
     *
     * @param factory
     * @param job
     */
    public void addEventTraceRdbDataSource(BeanDefinitionBuilder factory, Job job) {
        if (StringUtils.hasText(job.getEventTraceRdbDataSource())) {
            BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
            rdbFactory.addConstructorArgReference(job.getEventTraceRdbDataSource());
            factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
        }
    }

    /**
     * job监听器
     *
     * @param factory
     * @param job
     */
    public void addElasticJobListeners(BeanDefinitionBuilder factory, Job job) {
        List<BeanDefinition> elasticJobListeners = getTargetElasticJobListeners(job);
        factory.addConstructorArgValue(elasticJobListeners);
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
