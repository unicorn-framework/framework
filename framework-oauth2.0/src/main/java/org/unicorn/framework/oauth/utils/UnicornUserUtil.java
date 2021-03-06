package org.unicorn.framework.oauth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.unicorn.framework.oauth.dto.UnicornUser;

import java.security.Principal;

/**
 * @author xiebin
 */
@Slf4j
public class UnicornUserUtil {
    public static Long getUserId(Principal principal) {
        try {
            OAuth2Authentication oo = (OAuth2Authentication) principal;
            Long userId = null;
            if (oo != null) {
                UnicornUser user = (UnicornUser) oo.getUserAuthentication().getPrincipal();
                userId = user.getId();
            }
            return userId;
        } catch (Exception e) {
            log.warn("获取用户信息异常", e);
            return null;
        }
    }

    /**
     * 获取登录用户ID
     *
     * @return
     */
    public static Long getUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("是否以认证=======" + authentication.isAuthenticated());
            Long userId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                UnicornUser user = (UnicornUser) authentication.getPrincipal();
                userId = user.getId();
            }
            log.info("userId======"+userId);
            return userId;
        } catch (Exception e) {
            log.warn("获取用户信息异常", e);
            return null;
        }


    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static UnicornUser getLoginUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return (UnicornUser) authentication.getPrincipal();
        } catch (Exception e) {
            log.warn("获取用户信息异常", e);
            return null;
        }


    }
}
