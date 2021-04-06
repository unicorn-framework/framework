package org.unicorn.framework.elastic.dynamic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.tracing.api.TracingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.unicorn.framework.elastic.dynamic.bean.Job;

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

    @Autowired(required = false)
    private TracingConfiguration tracingConfig;

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


    private Object getBean(Job job) {
        Object obj = null;
        try {
            obj = ctx.getBean(job.getJobName());
        } catch (Exception e) {

        } finally {
            return obj;
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
                if (getBean(job) == null) {
                    defaultListableBeanFactory.registerSingleton(job.getJobName(), elasticjob);
                }
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
    public void removeJob(String jobName) {
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
     * jobCoreConfiguration 核心配置
     *
     * @param job
     * @return
     */
    public JobConfiguration jobCoreConfiguration(Job job) {
        JobConfiguration jobConfiguration =
                JobConfiguration.newBuilder(job.getJobName(), job.getShardingTotalCount())
                        .shardingItemParameters(job.getShardingItemParameters())
                        .description(job.getDescription())
                        .failover(job.isFailover())
                        .jobParameter(job.getJobParameter())
                        .misfire(job.isMisfire())
                        .cron(job.getCron())
                        .jobListenerTypes("unicornJobListener")
                        .
                        .build();
        if (tracingConfig != null) {
            jobConfiguration.getExtraConfigurations().add(tracingConfig);
        }
        return jobConfiguration;
    }

}
