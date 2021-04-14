package org.unicorn.framework.elastic.reg;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;

import java.util.List;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Slf4j
public abstract class AbstractJobReg implements IRegJob {


    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;


    @Override
    public void regJob(Job job, Object jobBean) {
        try {
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
        List<String> listeners = Lists.newArrayList();
        listeners.add("unicornJobListener");
        String jobName=job.getJobName();
        //如果是一次性任务 则修改jobName
        if(job.isOnce()){
            jobName=Contants.JOB_NAMESPACE+jobName;
        }
        JobConfiguration jobConfiguration =
                JobConfiguration.newBuilder(jobName, job.getShardingTotalCount())
                        .shardingItemParameters(job.getShardingItemParameters())
                        .description(job.getDescription())
                        .failover(job.isFailover())
                        .jobParameter(job.getJobParameter())
                        .misfire(job.isMisfire())
                        .cron(job.getCron())
                        .jobListenerTypes(listeners.toArray(new String[]{}))
                        .build();
        return jobConfiguration;
    }

    public CoordinatorRegistryCenter getCoordinatorRegistryCenter() {
        return this.coordinatorRegistryCenter;
    }
}
