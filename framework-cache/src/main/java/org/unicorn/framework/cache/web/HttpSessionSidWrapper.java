/**
 * Title: HttpSessionSidWrapper.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.cache.web;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.unicorn.framework.base.SpringContextHolder;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.core.exception.UnicornRuntimeException;
import org.unicorn.framework.util.common.Enumerator;

import lombok.extern.slf4j.Slf4j;

/**
 * Title: HttpSessionSidWrapper<br/>
 * Description: <br/>
 * 
 * @author Go
 * @date 2015年9月21日下午5:54:22
 *
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class HttpSessionSidWrapper extends HttpSessionWrapper {

    private String sid = "";

    private Map map = null;

    private SessionManager sessionManager;

    public HttpSessionSidWrapper(String sid, HttpSession session) {
        super(session);
        this.sid = sid;
        this.sessionManager = (SessionManager) SpringContextHolder.getBean(SessionManager.class);      
        try {
            this.map = sessionManager.getSession(sid);
        } catch (PendingException e) {
           log.error(e.getCode(), e.getMessage(),sid, e);
            throw new UnicornRuntimeException(e.getCode(),e);
        }
    }

    @Override
    public String getId() {
        return this.sid;
    }

    @Override
    public Object getAttribute(String arg0) {
        if (null == this.map) {
            log.info("session get", this.sid, arg0, null);
            return null;
        } else {
        	log.info("session get", this.sid, arg0,
                    this.map.get(arg0) == null ? "null" : this.map.get(arg0)
                            .toString());
        }
        return this.map.get(arg0);
    }

	@Override
    public Enumeration getAttributeNames() {
        return (new Enumerator(this.map.keySet(), true));
    }

    @Override
    public void invalidate() {
        this.map.clear();
        try {
        	log.info("session REMOVE ", sid, "");
            sessionManager.removeSession(sid);
        } catch (PendingException e) {
        	log.error(e.getCode(), e.getMessage(),sid, e);
            throw new UnicornRuntimeException(e.getCode(),e);
        }
    }

    @Override
    public void removeAttribute(String arg0) {
        log.info("session remove", this.sid, arg0);
        this.map.remove(arg0);
        try {
            sessionManager.setSession(this.sid, this.map);
        } catch (PendingException e) {
        	 log.error(e.getCode(), e.getMessage(),sid, e);
            throw new UnicornRuntimeException(e.getCode(),e);
        }
    }
    
    
    //需要将PedingException进行转换，为了复写的方法可用
    @SuppressWarnings("unchecked")
	@Override   
    public void setAttribute(String arg0, Object arg1) {
    	log.info("session set", this.sid, arg0,
                arg1 == null ? "null" : arg1.toString());
        this.map.put(arg0, arg1);
        try {
            sessionManager.setSession(this.sid, this.map);
        } catch (PendingException e) {
        	log.error(e.getCode(), e.getMessage(),sid, e);
            throw new UnicornRuntimeException(e.getCode(),e);
        }
    }

}
