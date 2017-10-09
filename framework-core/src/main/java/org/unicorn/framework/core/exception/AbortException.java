/**
 * Title: AbortException.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.core.exception;

/**
 * Title: AbortException<br/>
 * Description: <br/>
 * 
 * 
 * @author xiebin
 *
 */
public class AbortException extends DataAccessException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbortException(String code, String message) {
        super(code, message);
        this.code = code;
    }

    public AbortException(String code, String message, Class<?> clazz) {
        super(code, message);
        this.code = code;
    }

    public AbortException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public AbortException(String code, String message, Class<?> clazz, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public void catchLog(Class<?> clazz, String returnCode) {
    }

}
