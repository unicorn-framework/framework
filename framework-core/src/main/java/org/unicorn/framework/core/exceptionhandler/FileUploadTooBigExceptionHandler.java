//package org.unicorn.framework.core.exceptionhandler;
//
//import io.undertow.server.RequestTooBigException;
//import org.springframework.stereotype.Component;
//import org.unicorn.framework.core.ResponseDto;
//import org.unicorn.framework.core.SysCode;
//
///**
// * @author xiebin
// */
//@Component
//public class FileUploadTooBigExceptionHandler implements IExceptionHandler {
//    @Override
//    public boolean supports(Exception e) {
//        return (e.getCause() instanceof RequestTooBigException);
//    }
//
//    @Override
//    public ResponseDto<String> handler(Exception e) {
//        return new ResponseDto<>(SysCode.FILE_UPLOAD_TOO_BIG);
//    }
//}
