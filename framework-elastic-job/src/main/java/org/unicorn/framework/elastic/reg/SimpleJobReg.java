package org.unicorn.framework.elastic.reg;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ElasticJob;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
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
    public ScheduleJobBootstrap prepareScheduleJobBootstrap(Job job, Object jobBean)   throws  PendingException{
        try {
            //作业对象
            ElasticJob elasticjob = (ElasticJob) jobBean;
            return new ScheduleJobBootstrap(getCoordinatorRegistryCenter(), elasticjob, jobCoreConfiguration(job));
        } catch (Exception e) {
            log.error("【" + job.getJobName() + "】\t初始化失败", e);
            throw new PendingException(SysCode.SYS_FAIL, "\"【\" + job.getJobName() + \"】\\t初始化失败", e);
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
