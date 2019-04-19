package org.unicorn.framework.oauth.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.base.UnicornRequestContextHolder;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author xiebin
 */
@Configuration
public class UnicornFeignConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        Enumeration<String> headNames = request.getHeaderNames();
//        while (headNames.hasMoreElements()) {
//            String headName = headNames.nextElement();
//            requestTemplate.header(headName, request.getHeader(headName));
//        }
    }
}