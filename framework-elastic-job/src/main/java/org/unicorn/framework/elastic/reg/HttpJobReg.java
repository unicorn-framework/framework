package org.unicorn.framework.elastic.reg;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.http.props.HttpJobProperties;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.bean.UnicornHttpJob;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Component
@Slf4j
public class HttpJobReg extends AbstractJobReg {
    @Override
    public ScheduleJobBootstrap prepareScheduleJobBootstrap(Job job, Object jobBean) throws PendingException {
        try {
            UnicornHttpJob httpJob = (UnicornHttpJob) job;
            JobConfiguration jobConfiguration = jobCoreConfiguration(httpJob);
            jobConfiguration.getProps().setProperty(HttpJobProperties.URI_KEY, httpJob.getUrl());
            jobConfiguration.getProps().setProperty(HttpJobProperties.METHOD_KEY, httpJob.getMethod());
            jobConfiguration.getProps().setProperty(HttpJobProperties.DATA_KEY, httpJob.getData());
            jobConfiguration.getProps().setProperty(HttpJobProperties.CONNECT_TIMEOUT_KEY, httpJob.getConnectTimeout());
            jobConfiguration.getProps().setProperty(HttpJobProperties.READ_TIMEOUT_KEY, httpJob.getReadTimeout());
            jobConfiguration.getProps().setProperty(HttpJobProperties.CONTENT_TYPE_KEY, httpJob.getContentType());
            return new ScheduleJobBootstrap(getCoordinatorRegistryCenter(), "HTTP", jobConfiguration);

        } catch (Exception e) {
            log.error("【" + job.getJobName() + "】\tinit failure", e);
            throw new PendingException(SysCode.SYS_FAIL, "【" + job.getJobName() + "】\t 初始化失败", e);
        }
    }

    @Override
    public boolean support(Object jobBean, ElasticJobConf elasticJobConf) {
        return Contants.HTTP_JOB.equals(elasticJobConf.jobType());
    }
}
