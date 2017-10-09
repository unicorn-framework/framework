/**
 * Title: RollbackException.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.core.exception;

/**
 * Title: RollbackException<br/>
 * Description: 继承DataAccessException<br/>
 * 
 * 
 * @author xiebin
 *
 */
public class DataAccessException extends UnicornException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public DataAccessException(String code, String message) {
        super(code, message);

    }

    

    public DataAccessException(String code, Throwable throwable) {
        super(code, throwable);

    }

    public DataAccessException(String code, String message,
            Throwable throwable) {
        super(code,message, throwable);
        
    }

    public String getCode() {
        return this.code;
    }
}
