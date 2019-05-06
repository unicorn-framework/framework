package org.unicorn.framework.oss.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.unicorn.framework.core.ResponseDto;
import org.unicorn.framework.core.SysCode;

/**
 * @author xiebin
 */
@Slf4j
@ControllerAdvice
public class FileUploadToobigExcetionHandler extends ResponseEntityExceptionHandler {

    /**
     * 处理上传异常
     *
     * @param t
     * @return
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseDto<?>> handleAll(Throwable t) throws Exception {
        // TODO do Throwable t
        logger.error("=>" + t.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG), headers, HttpStatus.OK);
    }


}
