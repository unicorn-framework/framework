/**
 * Title: GoException.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 */
package org.unicorn.framework.core.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * 业务异常不进行降级
 * HystrixBadRequestException异常不会走hystrix降级流程，而是直接原样抛出异常
 * @author xiebin
 *
 */
public class UnicornException extends  HystrixBadRequestException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 标识是否需要弹出tips    true:需要   true:不需要
     * 默认弹出来 ==true
     */
    private Boolean tipsFlag;

    protected String code;

    protected Throwable nestedException = null;

    public Boolean getTipsFlag() {
        return tipsFlag;
    }

    public UnicornException(String code, String message) {
        this(code, message, true);
    }

    public UnicornException(String code, String message, Boolean tipsFlag) {
        super(message);
        this.code = code;
        this.tipsFlag = tipsFlag;
    }

    public UnicornException(String code, String message, Throwable throwable) {
        this(code, message, throwable, true);
    }

    public UnicornException(String code, String message, Throwable throwable, Boolean tipsFlag) {
        super(message, throwable);
        this.code = code;
        this.nestedException = throwable;
        this.tipsFlag = tipsFlag;
    }


    public Throwable getNestedException() {
        return nestedException;
    }

    public String getCode() {
        return this.code;
    }

}
