package org.unicorn.framework.mq.service;

import javax.jms.Destination;

import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.mq.domain.DestinactionDomain;

/**
 * 用来发送消息
 * @author xiebin
 *
 */
public interface IMessageProduerService {
	
    public void sendMessage(Destination destination, final String message) throws PendingException;
    
    public void sendMessage(DestinactionDomain destinactionDomain, final String message) throws PendingException;
}
