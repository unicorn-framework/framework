package org.unicorn.framework.elastic.parser;

import org.unicorn.framework.elastic.dynamic.bean.Job;

/**
 * 解析job的接口
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 9:55
 */
public interface IParserJob {

    boolean support(String jobType);

    Job parserJob(Object bean);


}
