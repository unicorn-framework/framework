/**
 * Title: SpringContextHolder.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 */
package org.unicorn.framework.web.utils.web;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Title: RequestUtil<br/>
 * Description: <br/>
 *
 * @author xiebin
 */

public class RequestUtil {


    public static HttpServletRequest getRrequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }


}
