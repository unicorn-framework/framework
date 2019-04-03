/**
 * Title: GoRuntimeException.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.core.exception;

/**
 * Description: 其他异常定义和异常收集器功能，自己本身就是一个RuntimeException
 * 作用1:可以进行包装，特别是在某些@Override方法中。参见类HttpSessionSidWrapper
 * <br/>
 * 
 * @author xiebin
 *
 */
public class UnicornRuntimeException extends RuntimeException{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    private String code;
    
    public PendingException toPenddingException(){
    	return new PendingException(this.code, getMessage());
    }
    
    public UnicornRuntimeException(){}
    
    public UnicornRuntimeException(UnicornException unicornException) {
        super(unicornException.getMessage());
        this.code = unicornException.getCode();
    }

    public UnicornRuntimeException(String code, String message) { 
        super(message);
        this.code = code;
    }

    public UnicornRuntimeException(String code,Throwable nestedException) {
        super(nestedException);        
        this.code = code;
    }

    public UnicornRuntimeException(String code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

}
