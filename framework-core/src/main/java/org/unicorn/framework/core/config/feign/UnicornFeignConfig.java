package org.unicorn.framework.core.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.properties.UnicornCoreProperties;
import org.unicorn.framework.core.utils.GrayUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xiebin
 */
@Configuration
@Slf4j
public class UnicornFeignConfig implements RequestInterceptor {
    /**
     * token 头部名
     */
    public static final String TOKEN_HEADER_NAME = "Authorization";
    @Autowired
    UnicornCoreProperties unicornCoreProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes == null) {
                log.info("attributes==" + attributes);
                return;
            }
            //设置token头
            HttpServletRequest request = attributes.getRequest();
            //feign请求模板
            Map<String, Collection<String>> headerMap = requestTemplate.request().headers();
            //获取oauth2.0授权头
            List<String> authorizationList = Collections.list(request.getHeaders(TOKEN_HEADER_NAME));
            //设置授权头信息
            if (!headerMap.containsKey(TOKEN_HEADER_NAME)) {
                requestTemplate.header(TOKEN_HEADER_NAME, authorizationList.toArray(new String[]{}));
            }
            //设置跟踪ID头部
            requestTemplate.header(UnicornConstants.REQUEST_TRACK_HEADER_NAME, request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
            //设置灰度上下文
            if (unicornCoreProperties.getEnable().equals(Boolean.TRUE)) {
                GrayUtil.setGrayContext(request);
            }
        } catch (Exception e) {
            log.error("requestTemplate设置错误", e);
        }
    }
}