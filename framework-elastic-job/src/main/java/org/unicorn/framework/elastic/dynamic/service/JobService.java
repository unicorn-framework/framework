package org.unicorn.framework.elastic.dynamic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.util.JobUtils;
import org.unicorn.framework.util.json.JsonUtils;

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
     * @param jobInfo
     */
    public void addJob(String jobInfo) {
        try {
            Job job = JsonUtils.fromJson(jobInfo, Job.class);
            Class clazz = Class.forName(job.getJobClass());
            Object bean = SpringContextHolder.getApplicationContext().getBean(clazz);
            Job realJob = JobUtils.getParserJob(bean).parserJob(bean, jobInfo);
            JobUtils.getRegJob(bean).regJob(realJob, bean);
            //动态job 持久化
            persistenceJob(realJob);
        } catch (Exception e) {
            throw new PendingException(SysCode.SYS_FAIL, "注册job失败", e);
        }

    }

    public Object getBean(Class jobBeanClass) {
        Object obj = null;
        try {
            obj = SpringContextHolder.getBean(jobBeanClass);
        } catch (Exception e) {
            log.info(jobBeanClass.getSimpleName() + "还没注册到容器中");
        } finally {
            return obj;
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
        try {
            coordinatorRegistryCenter.remove("/" + jobName);
            //
            if (iUnicornJobPersistenceService == null) {
                return;
            }

            Job job = new Job();
            job.setJobName(jobName);
            iUnicornJobPersistenceService.removeJob(job);
        } catch (Exception e) {
            log.error("job删除失败:jobName=" + jobName, e);
        }
    }
}
