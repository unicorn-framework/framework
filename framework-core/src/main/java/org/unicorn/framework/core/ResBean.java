package org.unicorn.framework.core;

import org.unicorn.framework.core.exception.PendingException;

/**
 * @author xiebin
 */
public class ResBean {

    private String code;
    private String info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ResBean(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static ResBean fromPendingException(PendingException pe) {
        return new ResBean(pe.getCode(), pe.getMessage());
    }

    /**
     * 将枚举类转换成异常抛出（失败时）
     *
     * @throws PendingException
     */
    public void throwException() throws PendingException {
        throw new PendingException(getCode(), getInfo());
    }

    /**
     * 将枚举类转换成异常抛出（失败时）
     *
     * @throws PendingException
     */
    public void throwException(String message) throws PendingException {
        throw new PendingException(getCode(), message);
    }

    public PendingException toException() {
        return new PendingException(getCode(), getInfo());
    }

    public PendingException toException(String message) {
        return new PendingException(getCode(), message);
    }

    public boolean isSuccess() {
        return getCode().equals(SysCode.SUCCESS);
    }


}
