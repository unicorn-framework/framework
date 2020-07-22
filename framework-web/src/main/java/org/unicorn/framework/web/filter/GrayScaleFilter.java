//package org.unicorn.framework.gateway.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import lombok.extern.slf4j.Slf4j;
//import org.unicorn.framework.core.exception.PendingException;
//import org.unicorn.framework.core.utils.GrayUtil;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author xiebin
// * 灰度过滤器
// */
//@Slf4j
//public class GrayScaleFilter extends ZuulFilter {
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
//        return true;
//    }
//
//    /**
//     * 过滤逻辑
//     *
//     * @return 过滤结果
//     */
//    @Override
//    public Object run() {
//        try {
//            RequestContext ctx = RequestContext.getCurrentContext();
//            HttpServletRequest request = ctx.getRequest();
//            //设置灰度上下文
//            GrayUtil.setGrayContext(request);
//        } catch (PendingException pe) {
//            throw pe;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//}