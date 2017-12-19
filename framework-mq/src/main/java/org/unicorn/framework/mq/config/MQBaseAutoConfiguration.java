package org.unicorn.framework.mq.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.mq.annotation.EnableMQConfiguration;
import org.unicorn.framework.mq.base.AbstractMQProducer;
import org.unicorn.framework.mq.base.AbstractMQPushConsumer;

/**
 * @author xiebin
 * RocketMQ配置文件
 */
@Configuration
@ConditionalOnBean(annotation = EnableMQConfiguration.class)
@AutoConfigureAfter({AbstractMQProducer.class, AbstractMQPushConsumer.class})
@EnableConfigurationProperties(MQProperties.class)
public class MQBaseAutoConfiguration implements ApplicationContextAware {
    @Autowired
    protected MQProperties mqProperties;
    protected ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    void registerBean(String beanName, Object bean) {
        applicationContext.getBeanFactory().registerSingleton(beanName, bean);
    }
}
