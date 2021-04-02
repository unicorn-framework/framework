package org.unicorn.framework.elastic.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.infra.listener.ElasticJobListener;
import org.apache.shardingsphere.elasticjob.infra.listener.ShardingContexts;
import org.springframework.stereotype.Component;

/**
 * 框架默认提供的job跟踪，记录job运行日志
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-04-01 14:20
 */
@Component
@Slf4j
public class UnicornJobListener implements ElasticJobListener {
    public UnicornJobListener(){}
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        log.info(shardingContexts.getJobName()+"开始运行");
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        log.info(shardingContexts.getJobName()+"运行结束");
    }

    @Override
    public String getType() {
        return "unicornJobListener";
    }
}
