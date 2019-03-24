package org.unicorn.framework.oauth.component;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.unicorn.framework.oauth.properties.OauthRequestTokenProperties;
import org.unicorn.framework.util.common.BeanMapping;

import javax.servlet.http.HttpServletRequest;

@Component
public class UnicornAccessTokenContext {
    @Autowired
    private OauthRequestTokenProperties oauthRequestTokenProperties;

    public String getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String accessToken = request.getHeader(oauthRequestTokenProperties.getTokenName());
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }
        int index = accessToken.lastIndexOf(":");
        if (index < 0) {
            return null;
        }
        return accessToken.substring(index + 1);
    }


    public static void main(String[] args) {

        System.out.println(BeanMapping.map("1111", Long.class));

    }
}
