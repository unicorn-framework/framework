package org.unicorn.framework.core.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.utils.GrayUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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
            String authorization = request.getHeader(TOKEN_HEADER_NAME);
            Map<String, Collection<String>> headerMap = requestTemplate.request().headers();
            if (!headerMap.containsKey(TOKEN_HEADER_NAME)) {
                requestTemplate.header(TOKEN_HEADER_NAME, authorization);
            }
            //设置跟踪ID头部
            requestTemplate.header(UnicornConstants.REQUEST_TRACK_HEADER_NAME, request.getHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME));
            //设置灰度上下文
            GrayUtil.setGrayContext(request);
        } catch (Exception e) {
            log.error("requestTemplate设置错误", e);
        }
    }
}