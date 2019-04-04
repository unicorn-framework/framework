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
        super(code, message);
        this.code = code;
    }

    public PendingException(ResBean resBean) {
        super(resBean.getCode(), resBean.getInfo());
        this.code = resBean.getCode();
    }

    public PendingException(ResponseDto<?> responseDto) {
        super(responseDto.getResCode(), responseDto.getResInfo());
        this.code = responseDto.getResCode();
    }


    public PendingException(String code, String message, Throwable throwable) {
        super(code, message, throwable);
        this.code = code;
    }


    public PendingException(ResBean resCode, String message) {
        super(resCode.getCode(), message);
        this.code = resCode.getCode();
    }


}
