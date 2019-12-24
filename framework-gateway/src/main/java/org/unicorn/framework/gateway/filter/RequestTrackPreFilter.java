package org.unicorn.framework.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.unicorn.framework.base.constants.UnicornConstants;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.core.handler.HandlerAdapter;
import org.unicorn.framework.core.handler.IHandler;
import org.unicorn.framework.core.utils.IdGeneratorSingleton;
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
 * 请求跟踪过滤器
 * 所有的资源请求在路由之前进行前置过滤 生成请求唯一id并传递给下游服务
 */
public class RequestTrackPreFilter extends ZuulFilter {


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
        return true;
    }

    /**
     * 过滤逻辑
     *
     * @return 过滤结果
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        context.set(UnicornConstants.REQUEST_TRACK_HEADER_NAME, "request:track:" + IdGeneratorSingleton.getInstance().generateKey().longValue());
        context.addZuulRequestHeader(UnicornConstants.REQUEST_TRACK_HEADER_NAME, "request:track:" + IdGeneratorSingleton.getInstance().generateKey().longValue());
        return null;
    }


}