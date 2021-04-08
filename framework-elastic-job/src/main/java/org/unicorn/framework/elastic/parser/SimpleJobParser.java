package org.unicorn.framework.elastic.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.bean.UnicornSimpleJob;
import org.unicorn.framework.util.json.JsonUtils;

/**
 * 简单job解析器
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Component
@Slf4j
public class SimpleJobParser extends AbstractParserJob {
    @Override
    public Job parserJobFromJobType(Object bean, String jobInfo) {
        try {
            UnicornSimpleJob simpleJob = new UnicornSimpleJob();
            if (StringUtils.isNotBlank(jobInfo)) {
                simpleJob = JsonUtils.fromJson(jobInfo, UnicornSimpleJob.class);
                return simpleJob;
            }
            setJobCommonProperties(simpleJob, bean);
            return simpleJob;
        } catch (Exception e) {
            log.error("job解析失败", e);
            throw new PendingException(SysCode.SYS_FAIL, e);
        }
    }

    @Override
    public boolean support(String jobType) {
        return Contants.SIMPLE_JOB.equals(jobType);
    }
}
