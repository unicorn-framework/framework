package org.unicorn.framework.elastic.reg;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Slf4j
public abstract class AbstractJobReg implements IRegJob {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;


    @Override
    public void regJob(Job job, Object jobBean) {
        try {
            if (job.isOnce()) {
                cacheService.put(job.getJobName(), job,-1, TimeUnit.SECONDS, Contants.JOB_NAMESPACE);
            }
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) SpringContextHolder.getApplicationContext().getAutowireCapableBeanFactory();
            ScheduleJobBootstrap scheduleJobBootstrap = prepareScheduleJobBootstrap(job, jobBean);
            defaultListableBeanFactory.registerSingleton(job.getJobName() + "UnicornJobScheduler", scheduleJobBootstrap);
            scheduleJobBootstrap.schedule();
            log.info("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit success");
        } catch (Exception e) {
            log.error("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit failure", e);
        }
    }

    /**
     * 准备 ScheduleJobBootstrap对象
     *
     * @param job
     * @param jobBean
     */
    public abstract ScheduleJobBootstrap prepareScheduleJobBootstrap(Job job, Object jobBean);

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
                        .build();
        return jobConfiguration;
    }

    public CoordinatorRegistryCenter getCoordinatorRegistryCenter() {
        return this.coordinatorRegistryCenter;
    }
}
