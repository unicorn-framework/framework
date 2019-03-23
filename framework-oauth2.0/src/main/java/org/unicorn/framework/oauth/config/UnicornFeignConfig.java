package org.unicorn.framework.oauth.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.oauth.properties.RequestTemplateProperties;

import javax.servlet.http.HttpServletRequest;

/**
 * @author  xiebin
 */
@Configuration
@EnableConfigurationProperties({RequestTemplateProperties.class })
public class UnicornFeignConfig implements RequestInterceptor {

    @Autowired
    private RequestTemplateProperties requestTemplateProperties;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //添加token
        requestTemplate.header(requestTemplateProperties.getTemplate(), request.getHeader(requestTemplateProperties.getRequest()));
    }
}