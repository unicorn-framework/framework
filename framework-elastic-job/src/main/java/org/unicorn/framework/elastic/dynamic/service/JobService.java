package org.unicorn.framework.elastic.dynamic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.util.JobUtils;

/**
 * @author xiebin
 */
@Service
@Slf4j
public class JobService {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;

    @Autowired(required = false)
    private IUnicornJobPersistenceService iUnicornJobPersistenceService;

    /**
     * 增加动态job
     *
     * @param job
     */
    public void addJob(Job job) {
        try {
            Class clazz = Class.forName(job.getJobClass());
            Object bean = clazz.newInstance();
            JobUtils.getParserJob(bean).parserJob(bean);
            JobUtils.getRegJob(bean).regJob(job, bean);
            //动态job 持久化
            persistenceJob(job);
        } catch (Exception e) {
            throw new PendingException(SysCode.SYS_FAIL, "job对应的类没找到");
        }

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
}
