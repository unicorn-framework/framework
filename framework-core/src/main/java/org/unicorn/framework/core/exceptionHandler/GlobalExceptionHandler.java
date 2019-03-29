package org.unicorn.framework.core.exceptionHandler;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.unicorn.framework.base.base.AbstractService;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.core.exception.UnicornRuntimeException;
import org.unicorn.framework.util.json.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * @author xiebin
 */
@ControllerAdvice
public class GlobalExceptionHandler extends AbstractService {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDto<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ResponseDto<String> resDto = new ResponseDto<>(SysCode.SYS_FAIL);
        resDto.setUrl(req.getRequestURL().toString());
        if (e instanceof NullPointerException) {
            resDto.setResCode(SysCode.SYS_NULL_POINT.getCode());
            resDto.setResInfo(SysCode.SYS_NULL_POINT.getInfo());
        } else if (e instanceof SQLException) {
            resDto.setResCode(SysCode.DB_ERROR.getCode());
            resDto.setResInfo(SysCode.DB_ERROR.getInfo());
        } else if (e instanceof PendingException) {
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
        } else if (e instanceof UnauthorizedException) {
            resDto.setResCode(SysCode.UNAUTHOR__ERROR.getCode());
            resDto.setResInfo(SysCode.UNAUTHOR__ERROR.getInfo());
        } else if (e instanceof UnauthenticatedException) {
            resDto.setResCode(SysCode.SESSION_ERROR.getCode());
            resDto.setResInfo(SysCode.SESSION_ERROR.getInfo());
        }  else {
            resDto.setResCode(SysCode.SYS_FAIL.getCode());
            resDto.setResInfo(e.getMessage());
        }
        error("异常信息:{}", JsonUtils.toJson(resDto), e);
        return resDto;
    }

}
