
package org.unicorn.framework.elastic.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.dynamic.service.JobService;

/**
 * @author xiebin
 * @date 2021-04-14 14:41
 * * @Description 用来清除一次性任务资源
 */
@Aspect
@Slf4j
@Component
public class UnicornJobExcuteAspect {

    @Pointcut("execution(* org.apache.shardingsphere.elasticjob.simple.job.SimpleJob.execute(..))")
    public void simpleJobPointCut() {
    }

    @Pointcut("execution(* org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob.processData(..))")
    public void dataflowJobPointCut() {
    }


    @After("simpleJobPointCut() || dataflowJobPointCut()")
    public void onceJobCleanProcess(JoinPoint jp) throws PendingException {
        Object obj = jp.getTarget();
        ElasticJobConf elasticJobConf = obj.getClass().getAnnotation(ElasticJobConf.class);
        if (!elasticJobConf.once()) {
            return;
        }
        //获取请求参数
        Object[] args = jp.getArgs();
        if (args == null) {
            return;
        }
        if (args.length == 0) {
            return;
        }
        if (!(args[0] instanceof ShardingContext)) {
            return;
        }
        ShardingContext shardingContexts = (ShardingContext) args[0];
//        try {
//            if (elasticJobConf.once()) {
//                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) SpringContextHolder.getApplicationContext().getAutowireCapableBeanFactory();
//                SpringContextHolder.getBean(shardingContexts.getJobName() + "UnicornJobScheduler", ScheduleJobBootstrap.class).shutdown();
////                defaultListableBeanFactory.destroySingleton(shardingContexts.getJobName() + "UnicornJobScheduler");
//                SpringContextHolder.getBean(JobService.class).removeJob(shardingContexts.getJobName());
//                log.info("一次性任务:" + shardingContexts.getJobName() + "清理完成");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
