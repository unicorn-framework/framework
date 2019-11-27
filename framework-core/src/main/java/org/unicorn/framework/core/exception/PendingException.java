package org.unicorn.framework.core.exception;

import org.unicorn.framework.core.ResBean;
import org.unicorn.framework.core.ResponseDto;

/**
 * @author xiebin
 */

public class PendingException extends UnicornException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PendingException(String code, String message) {
        this(code, message, true);
    }

    public PendingException(String code, String message, Boolean tipsFlag) {
        super(code, message, tipsFlag);
    }

    public PendingException(ResBean resBean) {
        this(resBean, true);
    }

    public PendingException(ResBean resBean, Boolean tipsFlag) {
        super(resBean.getCode(), resBean.getInfo(),tipsFlag);
        this.code = resBean.getCode();
    }



    public PendingException(ResponseDto<?> responseDto) {
        this(responseDto, true);
    }

    public PendingException(ResBean resBean,Throwable throwable) {
        this(resBean.getCode(), resBean.getInfo(),throwable,true);
        this.code = resBean.getCode();
    }
    public PendingException(ResBean resBean,Throwable throwable, Boolean tipsFlag) {
        this(resBean.getCode(), resBean.getInfo(),throwable,tipsFlag);
        this.code = resBean.getCode();
    }

    public PendingException(ResponseDto<?> responseDto, Boolean tipsFlag) {
        super(responseDto.getResCode(), responseDto.getResInfo(),tipsFlag);
        this.code = responseDto.getResCode();
    }

    public PendingException(String code, String message, Throwable throwable) {
        this(code,message,throwable,true);
    }

    public PendingException(String code, String message, Throwable throwable,Boolean tipsFlag) {
        super(code, message, throwable,tipsFlag);
        this.code = code;
    }


    public PendingException(ResBean resCode, String message) {
        this(resCode,message,true);
    }

    public PendingException(ResBean resCode, String message,Boolean tipsFlag) {
        super(resCode.getCode(), message,tipsFlag);
        this.code = resCode.getCode();
    }

    public PendingException(ResBean resCode, String message, Throwable throwable) {
        this(resCode,message,throwable,true);
    }

    public PendingException(ResBean resCode, String message, Throwable throwable,Boolean tipsFlag) {
        super(resCode.getCode(), message, throwable,tipsFlag);
        this.code = resCode.getCode();
    }
}
