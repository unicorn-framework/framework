//package org.unicorn.framework.oauth.security;
//
//import com.google.common.collect.Maps;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author xiebin
// */
//@Component
//@Slf4j
//public class UnicornInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
//    //存储url的权限
//    private HashMap<String, Collection<ConfigAttribute>> requestMap = null;
//
//
//    public void loadResourceDefine() {
//        requestMap = Maps.newHashMap();
//        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
//        ConfigAttribute cfg = new SecurityConfig("admin");
//        configAttributes.add(cfg);
//        requestMap.put("/api/userinfo/open/change/**/**", configAttributes);
//        requestMap.put("/open/homepopupsconfig/list", configAttributes);
//    }
//
//
//    /**
//     * 此方法是为了判断用户请求的url 是否在权限表中,如果在权限表中,则返回decide方法,用来判断用户是否具有此权限
//     * 当用户访问url时，通过url获取requestMap的其中的权限。
//     *
//     * @param object
//     * @return
//     * @throws IllegalArgumentException
//     */
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        if (requestMap == null) {
//            loadResourceDefine();
//        }
//        //object 中包含用户请求的request 信息  url
//        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
//        AntPathRequestMatcher matcher=new AntPathRequestMatcher("/**/open/**");
//        //open的接口不纳入权限控制
//        if (matcher.matches(request)){
//            return null;
//        }
//        for (Map.Entry<String, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
//            matcher = new AntPathRequestMatcher(entry.getKey());
//            if (matcher.matches(request)) {
//                return requestMap.get(entry.getKey());
//            }
//
//
//        }
//        return null;
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return true;
//    }
//}
