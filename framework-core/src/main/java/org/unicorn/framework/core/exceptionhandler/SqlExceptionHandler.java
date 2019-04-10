package org.unicorn.framework.core.exceptionhandler;

import org.springframework.stereotype.Component;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

import java.sql.SQLException;

/**
 * @author xiebin
 */
@Component
public class SqlExceptionHandler implements IExceptionHandler {
    @Override
    public boolean supports(Exception e) {
        return (e instanceof SQLException);
    }

    @Override
    public ResponseDto<String> handler(Exception e) {
        return new ResponseDto<>(SysCode.DB_ERROR);
    }
}
