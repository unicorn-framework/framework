package org.unicorn.framework.elastic.reg;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.elastic.dynamic.bean.Job;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
public abstract class AbstractJobReg implements IRegJob {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;

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

    public Object getBean(Job job) {
        Object obj = null;
        try {
            obj = SpringContextHolder.getBean(job.getJobName());
        } catch (Exception e) {

        } finally {
            return obj;
        }
    }
}
