package org.unicorn.framework.elastic.reg;

import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.elastic.annotation.ElasticJobConf;
import org.unicorn.framework.elastic.dynamic.bean.Job;

/**
 * 注册job的接口
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-07 9:55
 */
public interface IRegJob {

    boolean support(Object jobBean, ElasticJobConf elasticJobConf);

    void regJob(Job job, Object jobBean)throws PendingException;


}
