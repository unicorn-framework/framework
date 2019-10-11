package org.unicorn.framework.oauth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xiebin
 */
@Component
@Slf4j
public class UnicornInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private OAuth2Properties oAuth2Properties;
    /**
     * 此方法是为了判断用户请求的url 是否在权限表中,如果在权限表中,则返回decide方法,用来判断用户是否具有此权限
     * 当用户访问url时，通过url获取requestMap的其中的权限。
     *
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //检查是否开启检查，没开启则直接返回
        if(!oAuth2Properties.getPrivilegeCheck()){
            return null;
        }
        //object 中包含用户请求的request 信息  url
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        List<String> permitAlls=oAuth2Properties.getPermitAlls();
        permitAlls.add("/**/**.html");
        permitAlls.add("/**/**.js");
        permitAlls.add("/**/**.css");
        permitAlls.add("/**/**.ico");
        permitAlls.add("/**/**.ttf");
        permitAlls.add("/static/**");
        permitAlls.add("/**/swagger**");
        permitAlls.add("/**/v2/**");
        for(String antUrl:permitAlls){
            AntPathRequestMatcher matcher=new AntPathRequestMatcher(antUrl);
            //开放的接口不纳入权限控制
            if (matcher.matches(request)){
                return null;
            }
        }
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        ConfigAttribute cfg = new SecurityConfig("user");
        configAttributes.add(cfg);
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
