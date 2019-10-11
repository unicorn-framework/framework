package org.unicorn.framework.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.core.handler.HandlerAdapter;
import org.unicorn.framework.core.handler.IHandler;
import org.unicorn.framework.gateway.dto.BaseSecurityDto;
import org.unicorn.framework.gateway.properties.UnicornGatewaySecurityProperties;
import org.unicorn.framework.util.json.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

/**
 * @author xiebin
 * 资源安全过滤器
 * 所有的资源请求在路由之前进行前置过滤
 */
public class SecurityFilter extends ZuulFilter {
    @Autowired
    private UnicornGatewaySecurityProperties gatewaySecurityProperties;

    private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    /**
     * 过滤器的类型 pre表示请求在路由之前被过滤
     *
     * @return 类型
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器的执行顺序
     *
     * @return 顺序 数字越大表示优先级越低，越后执行
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器是否会被执行
     *
     * @return true
     */
    @Override
    public boolean shouldFilter() {
        return gatewaySecurityProperties.getSecurityCheckEnable();
    }

    /**
     * 过滤逻辑
     *
     * @return 过滤结果
     */
    @Override
    public Object run() {
        try {
            /**
             * 1、验证时间差   5分钟内
             * 2、验签
             */
            //构造 BaseSecurityDto
            BaseSecurityDto baseSecurityDto = genBaseSecurityDto();
            System.out.println("baseHeader==" + JsonUtils.toJson(baseSecurityDto));
            // 轮询处理
            pollingHandler(baseSecurityDto);
        } catch (PendingException pe) {
            throw pe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 轮询处理
     *
     * @param baseSecurityDto
     * @throws PendingException
     */
    private void pollingHandler(BaseSecurityDto baseSecurityDto) throws PendingException {
        //获取处理队列
        List<IHandler<BaseSecurityDto>> list = HandlerAdapter.handlerList(baseSecurityDto);
        //队列排序
        Collections.sort(list, (a, b) -> a.order().compareTo(b.order()));
        //轮询处理
        list.forEach(securityHandler -> {
            securityHandler.execute(baseSecurityDto);
        });
    }

    /**
     * 构造验证对象
     *
     * @return
     */
    private BaseSecurityDto genBaseSecurityDto() throws PendingException, Exception {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //token
        String accessToken = request.getHeader("Authorization");
        //timestamp 时间戳
        String timestamp = request.getHeader("timestamp");
        //sign 签名
        String sign = request.getHeader("sign");
        BaseSecurityDto baseSecurityDto = BaseSecurityDto.builder().sign(sign).timestamp(timestamp).token(accessToken).build();
        // 请求方法
        String method = request.getMethod();
        System.out.println("method==" + method);
        if ("GET".equalsIgnoreCase(method)) {
            System.out.println("param===" + request.getQueryString());
            baseSecurityDto.setQueryString(request.getQueryString());
        } else {
            // 获取请求的输入流
            InputStream in = request.getInputStream();
            String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
            baseSecurityDto.setBody(body);
            System.out.println("body==" + body);
        }
        return baseSecurityDto;
    }
}