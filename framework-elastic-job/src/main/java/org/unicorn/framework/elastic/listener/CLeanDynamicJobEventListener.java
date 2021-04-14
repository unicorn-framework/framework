package org.unicorn.framework.elastic.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.unicorn.framework.elastic.dynamic.service.JobService;
import org.unicorn.framework.elastic.event.CleanDynamicJobEvent;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-14 16:31
 */
@Component
@Slf4j
public class CLeanDynamicJobEventListener implements ApplicationListener<CleanDynamicJobEvent> {
    @Autowired
    private JobService jobService;

    @Override
    public void onApplicationEvent(CleanDynamicJobEvent cleanDynamicJobEvent) {
        jobService.removeJob(cleanDynamicJobEvent.getJobName());
    }
}
