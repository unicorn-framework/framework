package org.unicorn.framework.oauth.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.filter.OncePerRequestFilter;
import org.unicorn.framework.oauth.config.DeleteAuthorizationHttpServiceRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public UnicornPermitAuthenticationFilter() {
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            Authentication authentication = this.tokenExtractor.extract(request);
            OAuth2Authentication oAuth2AccessToken = tokenStore.readAuthentication(authentication.getPrincipal().toString());
            if (oAuth2AccessToken == null) {
                request = new DeleteAuthorizationHttpServiceRequestWrapper(request);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            request = new DeleteAuthorizationHttpServiceRequestWrapper(request);
        }

        chain.doFilter(request, response);
    }
}
