package org.unicorn.framework.elastic.dynamic.util;

import org.unicorn.framework.base.base.SpringContextHolder;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.parser.IParserJob;
import org.unicorn.framework.elastic.reg.IRegJob;

import java.util.Map;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 10:52
 */
public class JobUtils {

    public static IParserJob getParserJob(Object jobBean) throws PendingException {
        Class<?> clz = jobBean.getClass();
        ElasticJobConf conf = clz.getAnnotation(ElasticJobConf.class);
        Map<String, IParserJob> parserJobMap = SpringContextHolder.getApplicationContext().getBeansOfType(IParserJob.class);
        for (IParserJob parserJob : parserJobMap.values()) {
            if (parserJob.support(conf.jobType())) {
                return parserJob;
            }
        }
        throw new PendingException(SysCode.NOT_FOUND_HANDLER, "系统没有找到合适的job解析器");
    }

    public static IRegJob getRegJob(Object jobBean) throws PendingException {
        Class<?> clz = jobBean.getClass();
        ElasticJobConf conf = clz.getAnnotation(ElasticJobConf.class);
        Map<String, IRegJob> parserJobMap = SpringContextHolder.getApplicationContext().getBeansOfType(IRegJob.class);
        for (IRegJob regJob : parserJobMap.values()) {
            if (regJob.support(jobBean, conf)) {
                return regJob;
            }
        }
        throw new PendingException(SysCode.NOT_FOUND_HANDLER, "系统没有找到合适的job注册器");
    }

}
