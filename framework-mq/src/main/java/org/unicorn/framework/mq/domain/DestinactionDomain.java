package org.unicorn.framework.mq.domain;

import java.io.Serializable;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.unicorn.framework.enums.jms.JmsCommunicationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author xiebin
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DestinactionDomain implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private JmsCommunicationType jmsCommunicationType;
    private String destinactionName;
    public Destination getDestinaction(){
    	if(jmsCommunicationType.equals(JmsCommunicationType.P2P)){
    		return new ActiveMQQueue(this.getDestinactionName());
    	}else{
    		return new ActiveMQTopic(this.getDestinactionName());
    	}
    }
}
