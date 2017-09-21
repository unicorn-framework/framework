package org.unicorn.framework.core.exception;

import org.unicorn.framework.core.ResBean;

/**
 * 
 * @author xiebin
 *
 */

public class PendingException extends UnicornException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PendingException(String code, String message) {
		super(code, message);
		this.code = code;
	}

	public PendingException(ResBean resBean) {
		super(resBean.getCode(), resBean.getInfo());
		this.code = resBean.getCode();
	}

	public PendingException(String code, String message, Class<?> clazz) {
		super(code, message);
		this.code = code;
	}

	public PendingException(String code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public PendingException(String code, String message, Class<?> clazz, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public PendingException(ResBean resCode, String message) {
		super(resCode.getCode(), message);
		this.code = resCode.getCode();
	}

	public void catchLog(Class<?> clazz, String returnCode) {
	}

}
