package org.unicorn.framework.core.exceptionHandler;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;

/**
 * 
 * @author xiebin
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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
		} else {
			resDto.setResCode(SysCode.SYS_FAIL.getCode());
			resDto.setResInfo(SysCode.SYS_FAIL.getInfo());
		}

		return resDto;
	}

}
