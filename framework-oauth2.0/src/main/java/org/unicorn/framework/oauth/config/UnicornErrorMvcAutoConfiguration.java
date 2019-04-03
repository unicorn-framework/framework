//package org.unicorn.framework.oauth.config;
//
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.boot.autoconfigure.web.*;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.unicorn.framework.oauth.handler.UnicornErrorAttributes;
//
//import javax.servlet.Servlet;
//import java.util.List;
//
///**
// *
// */
//@Configuration
//@ConditionalOnWebApplication
//@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
//@AutoConfigureBefore(WebMvcAutoConfiguration.class)
//@EnableConfigurationProperties(ResourceProperties.class)
//public class UnicornErrorMvcAutoConfiguration extends ErrorMvcAutoConfiguration {
//    public UnicornErrorMvcAutoConfiguration(ServerProperties serverProperties,  ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
//     super(serverProperties,errorViewResolversProvider);
//    }
//    @Primary
//    @Bean
//    public UnicornErrorAttributes errorAttributes() {
//        return new UnicornErrorAttributes();
//    }
//}
