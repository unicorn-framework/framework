package org.unicorn.framework.elastic.reg;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Component
@Slf4j
public class SimpleJobReg extends AbstractJobReg {
    @Override
    public void regJob(Job job, Object jobBean) {
        try {
            ElasticJob elasticjob = (ElasticJob) jobBean;
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) SpringContextHolder.getApplicationContext().getAutowireCapableBeanFactory();
            if (getBean(job) == null) {
                defaultListableBeanFactory.registerSingleton(job.getJobName(), elasticjob);
            }
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(ScheduleJobBootstrap.class);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(getCoordinatorRegistryCenter());
            factory.addConstructorArgValue(elasticjob);
            factory.addConstructorArgValue(jobCoreConfiguration(job));

            defaultListableBeanFactory.registerBeanDefinition(job.getJobName() + "UnicornJobScheduler", factory.getBeanDefinition());
            ScheduleJobBootstrap scheduleJobBootstrap = (ScheduleJobBootstrap) SpringContextHolder.getBean(job.getJobName() + "UnicornJobScheduler");
            scheduleJobBootstrap.schedule();


            log.info("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit success");
        } catch (Exception e) {
            log.error("【" + job.getJobName() + "】\t" + job.getJobClass() + "\tinit failure", e);
        }
    }

    @Override
    public boolean support(Object jobBean, ElasticJobConf elasticJobConf) {
        return Contants.SIMPLE_JOB.equals(elasticJobConf.jobType()) && jobBean instanceof ElasticJob;
    }

    @Override
    public JobConfiguration jobCoreConfiguration(Job job) {
        return super.jobCoreConfiguration(job);
    }
}
