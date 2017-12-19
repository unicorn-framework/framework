package org.unicorn.framework.core.dto;

import java.io.Serializable;

import org.unicorn.framework.core.exception.PendingException;

/**
 * 
 * @author xiebin
 *
 */

public abstract class AbstractRequestDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void vaildatioinThrowException() throws PendingException;
}
