package org.unicorn.framework.oauth.filter;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.unicorn.framework.oauth.config.DeleteAuthorizationHttpServiceRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-01-07 16:22
 */
@Slf4j
public class UnicornPermitAuthenticationFilter extends OncePerRequestFilter {
    private TokenExtractor tokenExtractor = new BearerTokenExtractor();

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private TokenStore tokenStore;

    public List<String> getApmList() {
        return apmList;
    }


    private List<String> apmList = Lists.newArrayList();

    public UnicornPermitAuthenticationFilter() {
        apmList.add("/**/oauth/**");
        apmList.add("/**/token/**");
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (!enableHandle(request)) {
                chain.doFilter(request, response);
            }
            Authentication authentication = this.tokenExtractor.extract(request);
            OAuth2Authentication oAuth2AccessToken = tokenStore.readAuthentication(authentication.getPrincipal().toString());
            if (oAuth2AccessToken == null) {
                request = new DeleteAuthorizationHttpServiceRequestWrapper(request);
            }
        } catch (Exception e) {
            request = new DeleteAuthorizationHttpServiceRequestWrapper(request);
        }

        chain.doFilter(request, response);
    }

    /**
     * 判断是否交由此过滤器处理
     *
     * @param request
     * @return
     */
    public boolean enableHandle(HttpServletRequest request) {
        String url = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String path : getApmList()) {
            if (antPathMatcher.match(path, url)) {
                //如果匹配则不处理
                return false;
            }
        }
        return true;
    }

}
