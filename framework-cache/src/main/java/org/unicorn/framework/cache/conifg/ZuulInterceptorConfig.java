
package org.unicorn.framework.cache.conifg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.request.RequestContextHolder;

import com.google.common.base.Strings;

import feign.RequestInterceptor;

/**
*
*@author xiebin
*
*/
@Configuration
@Import(SessionPropertiesConfig.class)
public class ZuulInterceptorConfig  {
	
	@Autowired
	private SessionPropertiesConfig sessionPropertiesConfig;
	@Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            if (!Strings.isNullOrEmpty(sessionId)) {
                requestTemplate.header("Cookie", sessionPropertiesConfig.getCookieName()+"=" + sessionId);
                requestTemplate.header(sessionPropertiesConfig.getHeadName(),sessionId);
            }
        };
    }

	
//	@Bean
//    public RequestInterceptor headerInterceptor() {
//        return new RequestInterceptor() {
//            @Override
//            public void apply(RequestTemplate requestTemplate) {
//                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                        .getRequestAttributes();
//                HttpServletRequest request = attributes.getRequest();
//                Enumeration<String> headerNames = request.getHeaderNames();
//                if (headerNames != null) {
//                    while (headerNames.hasMoreElements()) {
//                        String name = headerNames.nextElement();
//                        String values = request.getHeader(name);
//                        System.out.println(name+"======"+values);
//                        requestTemplate.header(name, values);
//                    }
//                }
//            }
//        };
//    }
}

