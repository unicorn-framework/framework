package org.unicorn.framework.elastic.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.service.IUnicornJobPersistenceService;
import org.unicorn.framework.elastic.dynamic.service.JobService;
import org.unicorn.framework.util.json.JsonUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 初始化一次性任务
 * SmartLifecycle 生命周期 容器启动将会执行start方法
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-03-29 18:02
 */
@Component
public class UnicornInitOnceJob implements SmartLifecycle {

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    private JobService jobService;

    @Autowired(required = false)
    private IUnicornJobPersistenceService iUnicornJobPersistenceService;

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

            if (iUnicornJobPersistenceService == null) {
                return;
            }
            List<String> jobInfoList = iUnicornJobPersistenceService.jobInfoJsonList();
            jobInfoList.forEach(jobInfo -> {
                try {
                    Job job = JsonUtils.fromJson(jobInfo, Job.class);
//                    jobService.addJob(job);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

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
