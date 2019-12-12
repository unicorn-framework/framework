package org.unicorn.framework.mq.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.unicorn.framework.mq.annotation.MQProducer;
import org.unicorn.framework.mq.base.AbstractMQProducer;
import org.unicorn.framework.mq.service.impl.DefaultTransactionCheckListenerImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xiebin
 * 自动装配消息生产者
 */
@Slf4j
@Configuration
@ConditionalOnBean(MQBaseAutoConfiguration.class)
public class MQProducerAutoConfiguration extends MQBaseAutoConfiguration {

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(MQProducer.class);
        //对于仅仅只存在消息消费者的项目，无需构建生产者
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }
        if (producer == null) {
            Assert.notNull(mqProperties.getProducerGroup(), "producer group must be defined");
            Assert.notNull(mqProperties.getNameServerAddress(), "name server address must be defined");
            if (mqProperties.getTransactionEnable()) {
                producer = new TransactionMQProducer(mqProperties.getProducerGroup());
                TransactionMQProducer transactionMQProducer = (TransactionMQProducer) producer;
                transactionMQProducer.setCheckThreadPoolMinSize(mqProperties.getCheckThreadPoolMinSize());
                transactionMQProducer.setCheckThreadPoolMaxSize(mqProperties.getCheckThreadPoolMaxSize());
                transactionMQProducer.setCheckRequestHoldMax(mqProperties.getCheckRequestHoldMax());
                //事务回查检测器，是定时任务调用，所以会有线程池设置
                transactionMQProducer.setTransactionCheckListener(new DefaultTransactionCheckListenerImpl());
            } else {
                producer = new DefaultMQProducer(mqProperties.getProducerGroup());
            }
            producer.setNamesrvAddr(mqProperties.getNameServerAddress());
            producer.setSendMsgTimeout(mqProperties.getSendMsgTimeout());

            producer.setRetryTimesWhenSendFailed(3);
            producer.start();
        }
        // register default mq producer to spring context
        registerBean(DefaultMQProducer.class.getName(), producer);

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            publishProducer(entry.getKey(), entry.getValue());
        }
    }

    private void publishProducer(String beanName, Object bean) throws Exception {
        if (!AbstractMQProducer.class.isAssignableFrom(bean.getClass())) {
            throw new RuntimeException(beanName + " - producer未继承AbstractMQProducer");
        }
        AbstractMQProducer abstractMQProducer = (AbstractMQProducer) bean;
        abstractMQProducer.setProducer(producer);
        // begin build producer level topic
        MQProducer mqProducer = applicationContext.findAnnotationOnBean(beanName, MQProducer.class);
        String topic = mqProducer.topic();
        if (!StringUtils.isEmpty(topic)) {
            String transTopic = applicationContext.getEnvironment().getProperty(topic);
            if (StringUtils.isEmpty(transTopic)) {
                abstractMQProducer.setTopic(topic);
            } else {
                abstractMQProducer.setTopic(transTopic);
            }
        }
        // begin build producer level tag
        String tag = mqProducer.tag();
        if (!StringUtils.isEmpty(tag)) {
            String transTag = applicationContext.getEnvironment().getProperty(tag);
            if (StringUtils.isEmpty(transTag)) {
                abstractMQProducer.setTag(tag);
            } else {
                abstractMQProducer.setTag(transTag);
            }
        }
        log.info(String.format("%s is ready to produce message", beanName));
    }
}