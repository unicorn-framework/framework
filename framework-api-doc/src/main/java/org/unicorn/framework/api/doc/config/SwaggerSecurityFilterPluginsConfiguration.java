package org.unicorn.framework.api.doc.config;

import lombok.extern.slf4j.Slf4j;
import org.unicorn.framework.api.doc.utils.RequestUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author xiebin
 */
@Slf4j
public class SwaggerSecurityFilterPluginsConfiguration implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("==================== init swagger security filter plugin ====================");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpSession session = request.getSession();
        if (session == null || session.getAttribute(session.getId()) == null) {
            RequestUtils.writeForbidden(response);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("==================== destroy swagger security filter plugin ====================");
    }
}
