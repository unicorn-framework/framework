package org.unicorn.framework.elastic.init;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.util.JobUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 初始化周期性任务
 * SmartLifecycle 生命周期 容器启动将会执行start方法
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-03-29 18:02
 */
@Component
public class UnicornInitJob implements SmartLifecycle {

    private final AtomicBoolean running = new AtomicBoolean(false);


    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {

            //获取ElasticJobConf注解的类
            Map<String, Object> beanMap = SpringContextHolder.getApplicationContext().getBeansWithAnnotation(ElasticJobConf.class);
            for (Object confBean : beanMap.values()) {
                //解析job
                Job job = JobUtils.getParserJob(confBean).parserJob(confBean,null);
                //如果corn为空则不注册 没意义
                if (StringUtils.isBlank(job.getCron())) {
                    continue;
                }
                //注册job
                JobUtils.getRegJob(confBean).regJob(job, confBean);
            }

        }

    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {

        }

    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public int getPhase() {
        return 0;
    }

}
