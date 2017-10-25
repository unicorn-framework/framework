package org.unicorn.framework.mq.service.impl;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.mq.domain.DestinactionDomain;
import org.unicorn.framework.mq.service.IMessageProduerService;

/**
 * 用来发送消息
 * @author xiebin
 *
 */
@Service
public class MessageProduerService extends AbstractService implements IMessageProduerService{
	@Autowired 
    private JmsMessagingTemplate jmsTemplate;  
	
    public void sendMessage(Destination destination, final String message) throws PendingException{  
        try{
        	jmsTemplate.convertAndSend(destination, message);  
        }catch(Exception e){
        	SysCode.JMS_FAIL.throwException();
        }
    }

	@Override
	public void sendMessage(DestinactionDomain destinactionDomain, String message) throws PendingException {
		 try{
	        	jmsTemplate.convertAndSend(destinactionDomain.getDestinaction(), message);  
	        }catch(Exception e){
	        	SysCode.JMS_FAIL.throwException();
	        }
		
	}  
}
