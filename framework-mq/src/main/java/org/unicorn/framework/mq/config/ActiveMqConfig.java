package org.unicorn.framework.mq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 * 
 * @author xiebin
 *
 */
@Configuration
public class ActiveMqConfig {
	@Autowired
	ActiveMqProperties  activeMqProperties;
	@Bean
	public  PooledConnectionFactory pooledConnectionFactory(){
		PooledConnectionFactory pooledConnectionFactory=new PooledConnectionFactory();
		pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory());
		return pooledConnectionFactory;
	}
	
	public  ActiveMQConnectionFactory activeMQConnectionFactory(){
		ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory();
		String brokerUrl=activeMqProperties.getBrokerUrl();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName(activeMqProperties.getUser());
		activeMQConnectionFactory.setPassword(activeMqProperties.getPassword());
		return activeMQConnectionFactory;
	}
	
	@Bean
	public  JmsTemplate jmsTemplate(){
		JmsTemplate jmsTemplate=new JmsTemplate();
		jmsTemplate.setConnectionFactory(pooledConnectionFactory());
		return jmsTemplate;
	}
}
