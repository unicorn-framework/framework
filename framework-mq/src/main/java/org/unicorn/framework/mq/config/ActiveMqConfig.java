package org.unicorn.framework.mq.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.core.SysCode;

/**
 * 
 * @author xiebin
 *
 */
@Configuration
public class ActiveMqConfig  extends AbstractService{
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
		if(StringUtils.isBlank(brokerUrl)){
			error("消息 brokerUrl为空");
		}
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
