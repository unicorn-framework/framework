//package org.unicorn.framework.transaction.config;
//
//
//import com.codingapi.tx.config.service.TxManagerTxUrlService;
//import com.codingapi.tx.netty.service.TxManagerHttpRequestService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.unicorn.framework.transaction.service.UnicornTxManagerHttpRequestServiceImpl;
//import org.unicorn.framework.transaction.service.UnicornTxManagerTxUrlServiceImpl;
//
///**
// * @author xiebin
// */
//@Configuration
//public class UnicornTxConfig {
//    @Bean
//    public TxManagerHttpRequestService txManagerHttpRequestService(){
//        return new UnicornTxManagerHttpRequestServiceImpl();
//    }
//
//    @Bean
//    public TxManagerTxUrlService txManagerTxUrlService(){
//        return new UnicornTxManagerTxUrlServiceImpl();
//    }
//}
