package org.unicorn.framework.mq.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.unicorn.framework.mq.annotation.MQProducer;
import org.unicorn.framework.mq.base.AbstractMQProducer;

/**
 * @Author: xiebin
 * @Date: 2019/5/28 18:52
 * @Description: job 动态注册消息发送器
 */
@MQProducer(topic = "job", tag = "reg")
@Slf4j
public class DynamicJobRegProducer extends AbstractMQProducer {
    /**
     * 清除一次性任务
     *
     * @param jobInfo
     */
    public void sendRegJobMessage(String jobInfo) {
        super.asyncSend(jobInfo, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("动态注册消息消息发送成功:{}", sendResult.toString());
            }

            @Override
            public void onException(Throwable throwable) {
                //消息发送失败处理
                log.info("动态注册消息发送失败==>{}", throwable);
            }
        });
    }
}
