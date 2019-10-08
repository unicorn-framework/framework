package org.unicorn.framework.oauth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author  xiebin
 */
@Slf4j
@Service
public class UnicornFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private UnicornInvocationSecurityMetadataSourceService securityMetadataSource;

    /**
     *
     * @param unicornAccessDecisionManager
     */
    @Autowired
    public void setUnicornAccessDecisionManager(UnicornAccessDecisionManager unicornAccessDecisionManager) {
        super.setAccessDecisionManager(unicornAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        log.info("用户上下文信息:{}", SecurityContextHolder.getContext().getAuthentication());
        log.info("url地址:{}",fi.getHttpRequest().getRequestURI());
        log.info("客户端信息:{}", fi.getHttpRequest());
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = null;
        //这里要做非空判断，因为有可能 SecurityContextHolder.getContext().getAuthentication()获取为空，导致后面报错
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            token=super.beforeInvocation(fi);
        }
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }

    }


    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;

    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }
}
