package org.unicorn.framework.core.exceptionhandler;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.core.exception.UnicornRuntimeException;

import java.sql.SQLException;

/**
 * @author xiebin
 */
@Component
public class PendingExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        if (e instanceof PendingException) {
            return true;
        } else if (e.getCause() instanceof PendingException) {
            return true;
        } else if (e.getCause() instanceof UnicornRuntimeException) {
            return true;
        } else if (e instanceof UnicornRuntimeException) {
            return true;
        } else if (e instanceof UnauthorizedException) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseDto<String> handler(Exception e) {
        ResponseDto resDto = new ResponseDto(SysCode.SYS_FAIL, e.getMessage());
        if (e instanceof PendingException) {
            PendingException pe = (PendingException) e;
            resDto.setResCode(pe.getCode());
            resDto.setResInfo(pe.getMessage());
        } else if (e.getCause() instanceof PendingException) {
            PendingException pe = (PendingException) e.getCause();
            resDto.setResCode(pe.getCode());
            resDto.setResInfo(pe.getMessage());
        } else if (e.getCause() instanceof UnicornRuntimeException) {
            UnicornRuntimeException pe = (UnicornRuntimeException) e.getCause();
            resDto.setResCode(pe.getCode());
            resDto.setResInfo(pe.getMessage());
        } else if (e instanceof UnicornRuntimeException) {
            UnicornRuntimeException pe = (UnicornRuntimeException) e;
            resDto.setResCode(pe.getCode());
            resDto.setResInfo(pe.getMessage());
        }
        return resDto;
    }
}
