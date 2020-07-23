
package org.unicorn.framework.gateway.handler;

import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.exception.PendingException;

/**
 * @author xiebin
 */
public interface IGatewayHandler<S> {

    ResponseDto<?> execute(S s) throws PendingException;

    void vaildation(S s) throws PendingException;

    boolean supports(S s) throws PendingException;

    /**
     * Bean的顺序
     * @return
     * @throws PendingException
     */
    default Integer order() throws PendingException {
        return 0;
    }
}

