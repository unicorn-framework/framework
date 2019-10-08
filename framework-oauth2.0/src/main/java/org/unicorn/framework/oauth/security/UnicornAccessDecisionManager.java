package org.unicorn.framework.oauth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author xiebin
 */
@Component
@Slf4j
public class UnicornAccessDecisionManager implements AccessDecisionManager {
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
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        // 没有配置则直接放行
        if (null == configAttributes || configAttributes.size() <= 0 || authentication.getAuthorities().size() == 0) {
            return;
        }
        ConfigAttribute configAttribute;
        String needRole;
        for (Iterator<ConfigAttribute> iterable = configAttributes.iterator(); iterable.hasNext(); ) {
            configAttribute = iterable.next();
            String a = configAttribute.getAttribute() == null ? "" : configAttribute.getAttribute();
            needRole = a.replaceAll("\b", "");
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                // 循环添加到GrantedAuthority对象中的权限信息集合
                if (needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }

        throw new AccessDeniedException("没有权限访问！");
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
