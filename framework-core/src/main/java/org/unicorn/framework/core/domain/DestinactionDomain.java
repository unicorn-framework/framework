package org.unicorn.framework.core.domain;

import java.io.Serializable;

import org.unicorn.framework.base.enums.jms.JmsCommunicationType;

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
	/**
	 * 消息通讯类型
	 */
    private JmsCommunicationType jmsCommunicationType;
    /**
     * 消息目的地
     */
    private String destinactionName;
    /**
     * 消息体
     */
    private Object messageBody;
}
