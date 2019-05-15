package org.unicorn.framework.mq.base;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;

/**
 * @author xiebin
 * RocketMQ的消费者(Push模式)处理消息的接口
 */
@Slf4j
public abstract class AbstractMQPushConsumer<T> extends AbstractMQConsumer<T>{

    @Getter
    @Setter
    private DefaultMQPushConsumer consumer;

    public AbstractMQPushConsumer() {
    }

    /**
     * 继承这个方法处理消息
     * @see MessageExtConst
     * @param message 消息范型
     * @param extMap 存放消息附加属性的map, map中的key存放在 @link MessageExtConst 中
     * @return 处理结果
     */
    public abstract boolean process(T message, Map<String, Object> extMap);

    /**
     * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
     *
     * @param list 消息列表
     * @param consumeConcurrentlyContext 上下文
     * @return 消费状态
     */
    public ConsumeConcurrentlyStatus dealMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        for(MessageExt messageExt : list) {
            log.info("receive msgId: {}, tags : {}" , messageExt.getMsgId(), messageExt.getTags());
            int reconsumeTimes=messageExt.getReconsumeTimes();
            // parse message body
            T t = parseMessage(messageExt);
            // parse ext properties
            Map<String, Object> ext = parseExtParam(messageExt);
            if( null != t && !process(t, ext)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }
        return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
     *
     * @param list 消息列表
     * @param consumeOrderlyContext 上下文
     * @return 处理结果
     */
    public ConsumeOrderlyStatus dealMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
        for(MessageExt messageExt : list) {
            log.info("receive msgId: {}, tags : {}" , messageExt.getMsgId(), messageExt.getTags());
            T t = parseMessage(messageExt);
            Map<String, Object> ext = parseExtParam(messageExt);
            if( null != t && !process(t, ext)) {
                log.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }
        return  ConsumeOrderlyStatus.SUCCESS;
    }
}
