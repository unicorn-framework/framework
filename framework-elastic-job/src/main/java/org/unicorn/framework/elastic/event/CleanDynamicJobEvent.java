package org.unicorn.framework.elastic.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-14 16:26
 */
public class CleanDynamicJobEvent extends ApplicationEvent {
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    private String jobName;

    public CleanDynamicJobEvent(Object source, String jobName) {
        super(source);
        this.jobName = jobName;
    }

}
