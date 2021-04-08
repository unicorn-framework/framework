package org.unicorn.framework.elastic.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.base.JobAttributeTag;
import org.unicorn.framework.elastic.contants.Contants;
import org.unicorn.framework.elastic.dynamic.bean.Job;
import org.unicorn.framework.elastic.dynamic.bean.UnicornHttpJob;
import org.unicorn.framework.util.json.JsonUtils;

/**
 * http job解析器
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:14
 */
@Component
@Slf4j
public class HttpJobParser extends AbstractParserJob {
    @Override
    public Job parserJobFromJobType(Object bean, String jobInfo) throws  PendingException {
        try {
            UnicornHttpJob httpJob = new UnicornHttpJob();
            if (StringUtils.isNotBlank(jobInfo)) {
                httpJob = JsonUtils.fromJson(jobInfo, UnicornHttpJob.class);
                return httpJob;
            }
            setJobCommonProperties(httpJob, bean);
            httpJob.setUrl(getEnvironmentStringValue(httpJob.getJobName(), JobAttributeTag.URL, ""));
            httpJob.setMethod(getEnvironmentStringValue(httpJob.getJobName(), JobAttributeTag.METHOD, ""));
            httpJob.setData(getEnvironmentStringValue(httpJob.getJobName(), JobAttributeTag.DATA, ""));
            return httpJob;
        } catch (Exception e) {
            log.error("job解析失败", e);
            throw new PendingException(SysCode.SYS_FAIL, e);
        }
    }

    @Override
    public boolean support(String jobType) {
        return Contants.HTTP_JOB.equals(jobType);
    }


}
