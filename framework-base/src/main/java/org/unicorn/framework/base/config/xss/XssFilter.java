package org.unicorn.framework.base.config.xss;

import org.springframework.stereotype.Component;
import org.unicorn.framework.base.base.UnicornRequestContextHolder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author  xiebin
 */
@WebFilter(filterName = "xssFilter", urlPatterns = "/*", asyncSupported = true)
@Component
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }

}
