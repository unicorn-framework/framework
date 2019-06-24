/**
 * Title: GoException.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.core.exception;

/**
 * 业务异常不进行降级
 * HystrixBadRequestException异常不会走hystrix降级流程
 * @author xiebin
 *
 */
public class UnicornException extends RuntimeException{
       // HystrixBadRequestException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String    code;

    protected Throwable nestedException = null;

    public UnicornException(String code, String message) {
        super(message);
        this.code = code;
    }
    public UnicornException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
        this.nestedException = throwable;
    }

    public Throwable getNestedException() {
        return nestedException;
    }

    public String getCode() {
        return this.code;
    }

}
