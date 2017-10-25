package org.unicorn.framework.mq.service;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.unicorn.framework.base.AbstractService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

/**
 * 用来发送消息
 * @author xiebin
 *
 */
@Service
public class MessageProduerService extends AbstractService{
	@Autowired 
    private JmsMessagingTemplate jmsTemplate;  
	
    public void sendMessage(Destination destination, final String message) throws PendingException{  
        try{
        	jmsTemplate.convertAndSend(destination, message);  
        }catch(Exception e){
        	SysCode.JMS_FAIL.throwException();
        }
    }  
}
