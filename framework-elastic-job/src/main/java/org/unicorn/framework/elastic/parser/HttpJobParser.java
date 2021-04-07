package org.unicorn.framework.elastic.parser;

import org.springframework.stereotype.Component;
import org.unicorn.framework.elastic.base.JobAttributeTag;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.bean.UnicornHttpJob;

/**
 * http job解析器
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Component
public class HttpJobParser extends AbstractParserJob {
    @Override
    public Job parserJobFromJobType(Object bean) {
        return parserSimpleJob(bean);
    }

    @Override
    public boolean support(String jobType) {
        return Contants.HTTP_JOB.equals(jobType);
    }


    private Job parserSimpleJob(Object bean) {
        UnicornHttpJob httpJob = new UnicornHttpJob();
        setJobCommonProperties(httpJob, bean);
        httpJob.setUrl(getEnvironmentStringValue(httpJob.getJobName(), JobAttributeTag.URL, ""));
        httpJob.setMethod(getEnvironmentStringValue(httpJob.getJobName(), JobAttributeTag.METHOD, ""));
        httpJob.setData(getEnvironmentStringValue(httpJob.getJobName(), JobAttributeTag.DATA, ""));
        return httpJob;
    }
}
