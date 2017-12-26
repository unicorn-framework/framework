package org.unicorn.framework.cache.session;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.session.Session;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;

/**
 * 
 * @author xiebin
 *
 */
public class CookieAndHeadHttpSessionStrategy implements MultiHttpSessionStrategy{
	
	private List<HttpSessionStrategy> delegateList=new ArrayList<>();
	
	
	public void addHttpSessionStrategy(HttpSessionStrategy httpSessionStrategy){
		this.delegateList.add(httpSessionStrategy);
	}
	
	
 	public String getRequestedSessionId(HttpServletRequest request) {
		String sessionId="";
	Enumeration<String> es=	request.getHeaderNames();
		while(es.hasMoreElements()){
			String ss=es.nextElement();
			if("gid".equals(ss)){
				System.out.println(ss+"-->"+request.getHeader(ss));
			}
		}
			
		
		for(HttpSessionStrategy delegate:delegateList){
			sessionId= delegate.getRequestedSessionId(request);
			if(StringUtils.isNotBlank(sessionId)){
				break;
			}
		}
		return sessionId;
		
	}

	public void onNewSession(Session session, HttpServletRequest request,
			HttpServletResponse response) {
		for(HttpSessionStrategy delegate:delegateList){
			delegate.onNewSession(session, request, response);
		}
	}

	public void onInvalidateSession(HttpServletRequest request,
			HttpServletResponse response) {
		for(HttpSessionStrategy delegate:delegateList){
			delegate.onInvalidateSession(request, response);
		}
	}

	public HttpServletRequest wrapRequest(HttpServletRequest request,
			HttpServletResponse response) {
		return request;
	}

	public HttpServletResponse wrapResponse(HttpServletRequest request,
			HttpServletResponse response) {
		return response;
	}
	
	
	
	

}
