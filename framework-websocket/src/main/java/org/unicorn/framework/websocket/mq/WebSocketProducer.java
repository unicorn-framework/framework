package org.unicorn.framework.websocket.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.unicorn.framework.mq.annotation.MQProducer;
import org.unicorn.framework.mq.base.AbstractMQProducer;

/**
 * @Author: xiebin
 * @Description: webscoket广播消息
 */
@MQProducer(topic = "unicorn", tag = "websocket")
@Slf4j
public class WebSocketProducer extends AbstractMQProducer {

    public <T> void sendWebscoketMessage(T t) {
        super.asyncSend(t, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("失败", throwable);
            }
        });
    }
}
