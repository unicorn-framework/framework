package org.unicorn.framework.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.unicorn.framework.core.exception.PendingException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiebin
 * 灰度过滤器
 */
@Slf4j
public class GrayscaleFilter extends ZuulFilter {

    public static final String GRAY_FLAG = "enable";

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
        try {
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            //请求头部是否有名为gray-flag的请求头参数
            String grayFlag = request.getHeader("gray-flag");
            RibbonFilterContextHolder.clearCurrentContext();
            //如果为空直接返回
            if (StringUtils.isEmpty(grayFlag)) {
                return null;
            }
            //如果开启则走灰度节点
            RibbonFilterContextHolder.getCurrentContext().add("gray", grayFlag);

        } catch (PendingException pe) {
            throw pe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}