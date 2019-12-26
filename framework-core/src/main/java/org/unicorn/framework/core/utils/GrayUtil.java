package org.unicorn.framework.core.utils;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiebin
 */
public class GrayUtil {
    public static final String GRAY_FLAG = "enable";

    /**
     * 灰度时设置上下文
     * @param request
     */
    public static void setGrayContext(HttpServletRequest request) {
        //请求头部是否有名为gray-flag的请求头参数
        String grayFlag = request.getHeader("gray-flag");
        RibbonFilterContextHolder.clearCurrentContext();
        //如果为空直接返回
        if (GRAY_FLAG.equalsIgnoreCase(grayFlag)) {
            //如果开启则走灰度节点
            RibbonFilterContextHolder.getCurrentContext().add("gray", "true");
        } else {
            RibbonFilterContextHolder.getCurrentContext().add("gray", "false");
        }
    }
}
