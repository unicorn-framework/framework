package org.unicorn.framework.elastic.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.unicorn.framework.elastic.dynamic.service.IUnicornJobPersistenceService;
import org.unicorn.framework.elastic.dynamic.service.JobService;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 初始化 一次性任务
 * SmartLifecycle 生命周期 容器启动将会执行start方法
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-03-29 18:02
 */
@Component
public class UnicornInitOnceJob implements SmartLifecycle {
    @Autowired
    private JobService jobService;

    private final AtomicBoolean running = new AtomicBoolean(false);

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
            //获取本地存储的一次性任务
            List<String> jobInfoList = iUnicornJobPersistenceService.jobInfoJsonList();
            jobInfoList.forEach(jobInfo -> {
                //轮询一次性任务重新注册到调度中心
                jobService.addJob(jobInfo);
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
