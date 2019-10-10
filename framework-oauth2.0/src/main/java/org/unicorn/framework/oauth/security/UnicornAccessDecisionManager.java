package org.unicorn.framework.oauth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.core.SysCode;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.oauth.constants.OauthConstants;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xiebin
 */
@Component
@Slf4j
public class UnicornAccessDecisionManager implements AccessDecisionManager {
    @Autowired
    private OAuth2Properties oAuth2Properties;
    @Autowired
    private CacheService cacheService;

    /***
     *
     *
     * @param authentication  登陆系统用户的权限
     *
     * @param object
     * @param configAttributes url的权限
     *
     * @throws AccessDeniedException
     *             配置属性
     * @throws InsufficientAuthenticationException
     */

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //检查是否开启检查，没开启则直接返回
        if(!oAuth2Properties.getPrivilegeCheck()){
            return;
        }
        // 直接放行
        if (CollectionUtils.isEmpty(authentication.getAuthorities())) {
            return;
        }
        FilterInvocation fi = (FilterInvocation) object;
        Map<String, List<String>> map = (Map<String, List<String>>) cacheService.get(OauthConstants.PRIVILEGE_KEY, OauthConstants.PRIVILEGE_NAMESPACE);
        if (map == null) {
            throw new PendingException(SysCode.UNAUTHOR__ERROR);
        }
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            List<String> list = map.get(ga.getAuthority());
            if (!CollectionUtils.isEmpty(list)) {
                for (String url : list) {
                    AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
                    if (matcher.matches(fi.getHttpRequest())) {
                        return;
                    }
                }
            }
        }
        throw new PendingException(SysCode.UNAUTHOR__ERROR);
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
