package org.unicorn.framework.elastic.parser;

import org.springframework.stereotype.Component;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.bean.UnicornSimpleJob;

/**
 * 简单job解析器
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Component
public class SimpleJobParser extends AbstractParserJob {
    @Override
    public Job parserJobFromJobType(Object bean) {
        return parserSimpleJob(bean);
    }

    @Override
    public boolean support(String jobType) {
        return Contants.SIMPLE_JOB.equals(jobType);
    }


    private Job parserSimpleJob(Object bean) {
        UnicornSimpleJob simpleJob = new UnicornSimpleJob();
        setJobCommonProperties(simpleJob, bean);
        return simpleJob;
    }
}
