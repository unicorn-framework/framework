
package org.unicorn.framework.core.handler;

import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.exception.PendingException;

/**
*
*@author xiebin
*
*/
public interface IHandler<S> {

	public  ResponseDto<?> execute(S s) throws PendingException;
	public    void vaildation(S s) throws PendingException;
	public   boolean supports(S s) throws PendingException;
}

