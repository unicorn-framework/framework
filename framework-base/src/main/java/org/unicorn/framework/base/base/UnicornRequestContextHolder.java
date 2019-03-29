package org.unicorn.framework.base.base;

import com.alibaba.ttl.TransmittableThreadLocal;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来在多线程环境下 线程切换 ThreadLocal获取不到信息的问题
 * @author xiebin
 */
public class UnicornRequestContextHolder {

    private static final ThreadLocal<HttpServletRequest> context = new TransmittableThreadLocal<>();
    public static HttpServletRequest  getRequest(){
        return context.get();
    }
    public static void  setRequest(HttpServletRequest request){
         context.set(request);
    }
    public static void clear() {
        context.set(null);
    }
}
