package org.unicorn.framework.elastic.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener;
import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.service.JobService;
import org.unicorn.framework.elastic.event.CleanDynamicJobEvent;

/**
 * 框架默认提供的job跟踪，记录job运行日志
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-01 14:20
 */
@Component
@Slf4j
public class UnicornJobListener implements ElasticJobListener {

    public UnicornJobListener() {
    }

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        log.info(shardingContexts.getJobName() + "开始运行");
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        try {
            //如果是一次性任务则清理
            if (shardingContexts.getJobName().startsWith(Contants.JOB_NAMESPACE)) {
                String jobName = shardingContexts.getJobName().replaceFirst(Contants.JOB_NAMESPACE, "");
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) SpringContextHolder.getApplicationContext().getAutowireCapableBeanFactory();
                SpringContextHolder.getBean(jobName + "UnicornJobScheduler", ScheduleJobBootstrap.class).shutdown();
                defaultListableBeanFactory.destroySingleton(jobName + "UnicornJobScheduler");
                CleanDynamicJobEvent cleanDynamicJobEvent=new CleanDynamicJobEvent(this,shardingContexts.getJobName());
                SpringContextHolder.getApplicationContext().publishEvent(cleanDynamicJobEvent);
                log.info("一次性任务:" + shardingContexts.getJobName() + "清理完成");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(shardingContexts.getJobName() + "运行结束");
    }

    @Override
    public String getType() {
        return "unicornJobListener";
    }
}
