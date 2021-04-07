package org.unicorn.framework.elastic.reg;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.http.props.HttpJobProperties;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.springframework.stereotype.Component;
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
    public void regJob(Job job, Object jobBean) {
        try {
            UnicornHttpJob httpJob = (UnicornHttpJob) job;
            new ScheduleJobBootstrap(getCoordinatorRegistryCenter(), "HTTP", JobConfiguration.newBuilder(job.getJobName(), job.getShardingTotalCount())
                    .setProperty(HttpJobProperties.URI_KEY, httpJob.getUrl())
                    .setProperty(HttpJobProperties.METHOD_KEY, httpJob.getMethod())
                    .setProperty(HttpJobProperties.DATA_KEY, httpJob.getData())
                    .setProperty(HttpJobProperties.CONNECT_TIMEOUT_KEY, httpJob.getConnectTimeout())
                    .setProperty(HttpJobProperties.READ_TIMEOUT_KEY, httpJob.getReadTimeout())
                    .setProperty(HttpJobProperties.CONTENT_TYPE_KEY, httpJob.getContentType())
                    .cron(httpJob.getCron()).shardingItemParameters(httpJob.getShardingItemParameters()).build()).schedule();
            log.info("【" + job.getJobName() + "】\tinit success");
        } catch (Exception e) {
            log.error("【" + job.getJobName() + "】\tinit failure", e);
        }
    }

    @Override
    public boolean support(Object jobBean, ElasticJobConf elasticJobConf) {
        return Contants.HTTP_JOB.equals(elasticJobConf.jobType());
    }
}
