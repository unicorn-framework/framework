package org.unicorn.framework.elastic.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.unicorn.framework.elastic.dynamic.service.JobService;
import org.unicorn.framework.mq.annotation.MQConsumer;
import org.unicorn.framework.mq.base.AbstractMQPushConsumer;
import org.unicorn.framework.mq.base.MessageExtConst;

import java.util.Map;

/**
 * @Author: xiebin
 * @Date: 2021 04/14 18:23
 * @Description: 添加动态任务消费
 */
@Slf4j
@MQConsumer(consumerGroup = "reg_job", topic = "job", tag = "reg", messageMode = MessageExtConst.MESSAGE_MODE_BROADCASTING)
public class DynamicJobRegConsumer extends AbstractMQPushConsumer<String> {
    @Autowired
    private JobService jobService;

    @Override
    public boolean process(String message, Map<String, Object> extMap) {

        log.info("注册动态任务消费开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        try {
            jobService.addJob(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("注册动态任务消费异常:{}", e);
            return false;
        }

    }
}
