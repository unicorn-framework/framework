package org.unicorn.framework.oauth.filter;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 在认证过滤器之前处理
 *
 * @author xiebin
 */
@Slf4j
public class PermitAuthenticationFilter extends OncePerRequestFilter {
    /**
     * 请求匹配
     */
    private RequestMatcher requestMatcher;


    private OAuth2Properties oAuth2Properties;

    public PermitAuthenticationFilter(OAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
        List<String> permitAllList = oAuth2Properties.getPermitAlls();
        List<RequestMatcher> antPathRequestMatcherList = Lists.newArrayList();
        if (permitAllList != null) {
            permitAllList.forEach(antUrl -> {
                antPathRequestMatcherList.add(new AntPathRequestMatcher(antUrl));
            });
        }
        if (!antPathRequestMatcherList.isEmpty()) {
            this.requestMatcher = new OrRequestMatcher(antPathRequestMatcherList);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("当前访问的地址:{}", request.getRequestURI());
        if (requestMatcher.matches(request)) {
            request = new HttpServletRequestWrapper(request) {
                private Set<String> headerNameSet;

                @Override
                public Enumeration<String> getHeaderNames() {
                    if (headerNameSet == null) {
                        // first time this method is called, cache the wrapped request's header names:
                        headerNameSet = new HashSet<>();
                        Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
                        while (wrappedHeaderNames.hasMoreElements()) {
                            String headerName = wrappedHeaderNames.nextElement();
                            if (!getHeadName().equalsIgnoreCase(headerName)) {
                                headerNameSet.add(headerName);
                            }
                        }
                    }
                    return Collections.enumeration(headerNameSet);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    if (getHeadName().equalsIgnoreCase(name)) {
                        return Collections.<String>emptyEnumeration();
                    }
                    return super.getHeaders(name);
                }

                @Override
                public String getHeader(String name) {
                    if (getHeadName().equalsIgnoreCase(name)) {
                        return null;
                    }
                    return super.getHeader(name);
                }
            };

        }
        filterChain.doFilter(request, response);

    }

    /**
     * 获取请求头部名称
     *
     * @return
     */
    private String getHeadName() {
        return oAuth2Properties.getHeadName();
    }
}