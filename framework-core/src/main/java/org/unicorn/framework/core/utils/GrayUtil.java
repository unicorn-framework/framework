package org.unicorn.framework.core.utils;

import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiebin
 */
public class GrayUtil {

    /**
     * 灰度标识头名
     */
    public static final String GRAY_HEAD_NAME = "gray-flag";
    /**
     * 灰度标识头值
     */
    public static final String GRAY_HEAD_VALUE = "enable";
    /**
     * metadata-map key值
     */
    public static final String GRAY_META_KEY = "gray";

    /**
     * 灰度时设置上下文
     *
     * @param request
     */
    public static void setGrayContext(HttpServletRequest request) {
        //请求头部是否有名为gray-flag的请求头参数
        String grayFlag = request.getHeader(GRAY_HEAD_NAME);
        RibbonFilterContextHolder.clearCurrentContext();
        if (GRAY_HEAD_VALUE.equalsIgnoreCase(grayFlag)) {
            //如果开启则走灰度节点
            RibbonFilterContextHolder.getCurrentContext().add(GRAY_META_KEY, Boolean.TRUE.toString());
        } else {
            //否则走非灰度节点
            RibbonFilterContextHolder.getCurrentContext().add(GRAY_META_KEY, Boolean.FALSE.toString());
        }
    }
}
