/**
 * Title: HttpServletRequestWrapper.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.cache.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Title: HttpServletRequestWrapper<br/>
 * Description: <br/>
 * @author xiebin
 *
 */
public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
    String sid = "";

    public HttpServletRequestWrapper(String sid, HttpServletRequest arg0) {
        super(arg0);
        this.sid = sid;
    }

    @Override
    public HttpSession getSession(boolean create) {
        
        return new HttpSessionSidWrapper(this.sid, super.getSession(create));
    }

    @Override
    public HttpSession getSession() {
        return new HttpSessionSidWrapper(this.sid, super.getSession());
    }
}
