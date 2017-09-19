/**
 * Title: GoException.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.core.exception;

/**
 * 
 * @author xiebin
 *
 */
public class UnicornException extends Exception {

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

    public UnicornException(String code, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.nestedException = throwable;
    }

    public UnicornException(String code, String message, Throwable throwable) {
        this(message, throwable);
        this.code = code;
    }

    public Throwable getNestedException() {
        return nestedException;
    }

    public String getCode() {
        return this.code;
    }

}
