package org.unicorn.framework.mq.service.impl;

import org.springframework.stereotype.Component;
import org.unicorn.framework.mq.annotation.MQProducer;
import org.unicorn.framework.mq.base.AbstractMQProducer;

/**
 * 
 * @author xiebin
 *
 */
@Component
@MQProducer(topic = "test_topic", tag = "test-tag")
public class ProducerRocketMqService extends AbstractMQProducer {

}
