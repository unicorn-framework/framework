package org.unicorn.framework.core.dto;

import org.unicorn.framework.core.exception.PendingException;

import java.io.Serializable;

/**
 * @author xiebin
 */

public abstract class AbstractRequestDto implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 验证
     *
     * @throws PendingException
     */
    public abstract void vaildatioinThrowException() throws PendingException;
}
