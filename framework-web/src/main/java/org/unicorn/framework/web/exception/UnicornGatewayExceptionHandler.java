//package org.unicorn.framework.gateway.exception;
//
//import org.springframework.stereotype.Component;
//import org.unicorn.framework.core.ResponseDto;
//import org.unicorn.framework.core.exceptionhandler.IExceptionHandler;
//
///**
// * @author xiebin
// */
//@Component
//public class UnicornGatewayExceptionHandler implements IExceptionHandler {
//    @Override
//    public boolean supports(Exception e) {
//        return (e instanceof UnicornGatewayException);
//    }
//
//    @Override
//    public ResponseDto<String> handler(Exception e,String url) {
//        UnicornGatewayException unicornGatewayException=(UnicornGatewayException)e;
//        ResponseDto  resDto =new ResponseDto<>(unicornGatewayException.getPe().getCode(),unicornGatewayException.getPe().getMessage());
//        resDto.setUrl(url);
//        return resDto;
//    }
//}
