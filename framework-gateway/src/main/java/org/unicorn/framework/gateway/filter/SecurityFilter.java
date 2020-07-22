//package org.unicorn.framework.gateway.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.util.Lists;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.StreamUtils;
//import org.unicorn.framework.core.SysCode;
//import org.unicorn.framework.core.exception.PendingException;
//import org.unicorn.framework.core.handler.HandlerAdapter;
//import org.unicorn.framework.core.handler.IHandler;
//import org.unicorn.framework.gateway.dto.BaseSecurityDto;
//import org.unicorn.framework.gateway.exception.UnicornGatewayException;
//import org.unicorn.framework.gateway.properties.UnicornGatewaySecurityProperties;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.util.Collections;
//import java.util.List;
//
///**
// * @author xiebin
// * 资源安全过滤器
// * 所有的资源请求在路由之前进行前置过滤
// */
//@Slf4j
//
//public class SecurityFilter extends ZuulFilter {
//    @Autowired
//    private UnicornGatewaySecurityProperties unicornGatewaySecurityProperties;
//
//
//    private AntPathMatcher antPathMatcher;
//
//    private List<String> ignoreUrls = Lists.newArrayList();
//
//    public SecurityFilter() {
//        this.antPathMatcher = new AntPathMatcher();
//
//    }
//
//    @PostConstruct
//    public void initIgnoreUrls() {
//        ignoreUrls.add("/static/**");
//        ignoreUrls.add("/**/*.html");
//        ignoreUrls.add("/**/*.js");
//        ignoreUrls.add("/**/*.css");
//        ignoreUrls.add("/**/*.ico");
//        ignoreUrls.add("/**/*.ttf");
//        ignoreUrls.add("/**/*.ico");
//        ignoreUrls.add("/**/static/**");
//        ignoreUrls.add("/**/swagger**");
//        ignoreUrls.add("/**/v2/**");
//        ignoreUrls.addAll(unicornGatewaySecurityProperties.getIgnoreUrls());
//    }
//
//    public List<String> getIgnoreUrls() {
//        return ignoreUrls;
//    }
//
//    /**
//     * 过滤器的类型 pre表示请求在路由之前被过滤
//     *
//     * @return 类型
//     */
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    /**
//     * 过滤器的执行顺序
//     *
//     * @return 顺序 数字越大表示优先级越低，越后执行
//     */
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    /**
//     * 过滤器是否会被执行
//     *
//     * @return true
//     */
//    @Override
//    public boolean shouldFilter() {
//        try {
//
//            //未开启则直接返回false
//            if (!unicornGatewaySecurityProperties.getEnable()) {
//                return false;
//            }
//            RequestContext requestContext = RequestContext.getCurrentContext();
//            HttpServletRequest request = requestContext.getRequest();
//            String ignoreSignHead = request.getHeader("ignoreSignHead");
//            if (unicornGatewaySecurityProperties.getIgnoreSignHeadValue().equalsIgnoreCase(ignoreSignHead)) {
//                return false;
//            }
//            String requestUrl = request.getRequestURI();
//            //获取忽略url
//            List<String> ignoreUrls = getIgnoreUrls();
//            boolean flag = true;
//            for (String pattern : ignoreUrls) {
//                if (this.antPathMatcher.match(pattern, requestUrl)) {
//                    flag = false;
//                    break;
//                }
//            }
//            return flag;
//        } catch (Exception e) {
//            log.error("是否需要安全检查异常:", e);
//            return true;
//        }
//    }
//
//    /**
//     * 过滤逻辑
//     *
//     * @return 过滤结果
//     */
//    @Override
//    public Object run() throws ZuulException {
//        try {
//            //构造 BaseSecurityDto
//            BaseSecurityDto baseSecurityDto = genBaseSecurityDto();
//            //参数检查
//            baseSecurityDto.vaildatioinThrowException();
//            // 轮询处理
//            pollingHandler(baseSecurityDto);
//        } catch (PendingException pe) {
//            log.error("安全拦截异常:", pe);
//            throw new UnicornGatewayException(pe);
//        } catch (Exception e) {
//            log.error("安全拦截异常:", e);
//            throw new UnicornGatewayException(new PendingException(SysCode.API_SECURITY_ERROR));
//        }
//        return null;
//    }
//
//    /**
//     * 轮询处理
//     *
//     * @param baseSecurityDto
//     * @throws PendingException
//     */
//    private void pollingHandler(BaseSecurityDto baseSecurityDto) throws PendingException {
//        //获取处理队列
//        List<IHandler<BaseSecurityDto>> list = HandlerAdapter.handlerList(baseSecurityDto);
//        //队列排序
//        Collections.sort(list, (a, b) -> a.order().compareTo(b.order()));
//        //轮询处理
//        list.forEach(securityHandler -> {
//            securityHandler.execute(baseSecurityDto);
//        });
//    }
//
//    /**
//     * 构造验证对象
//     *
//     * @return
//     */
//    private BaseSecurityDto genBaseSecurityDto() throws PendingException, Exception {
//        RequestContext requestContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = requestContext.getRequest();
//        log.info("url====" + request.getRequestURL());
//        //appKey
//        String appKey = unicornGatewaySecurityProperties.getAppKey();
//        //timestamp 时间戳
//        String timestamp = request.getHeader("timestamp");
//        //随机字符串
//        String nonceStr = request.getHeader("nonceStr");
//        //sign 签名
//        String sign = request.getHeader("sign");
//        BaseSecurityDto baseSecurityDto = BaseSecurityDto.builder()
//                .sign(sign)
//                .timestamp(timestamp)
//                .appKey(appKey)
//                .nonceStr(nonceStr).build();
//        log.info("baseHeader==" + baseSecurityDto.toString());
//        return baseSecurityDto;
//    }
//
//    /**
//     * 视情况而定
//     *
//     * @param request
//     * @throws IOException
//     */
//    private void setParamter(HttpServletRequest request) throws IOException {
//        String method = request.getMethod();
//        log.info("http request method==" + method);
//        if ("GET".equalsIgnoreCase(method)) {
//            log.info("http request param===" + request.getQueryString());
//        } else {
//            // 获取请求的输入流
//            InputStream in = request.getInputStream();
//            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
//            log.info("http request body==" + body);
//        }
//    }
//
//
//}