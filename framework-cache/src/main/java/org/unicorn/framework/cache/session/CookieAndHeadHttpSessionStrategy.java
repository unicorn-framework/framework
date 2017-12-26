package org.unicorn.framework.cache.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.session.Session;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;
import org.unicorn.framework.base.AbstractService;

/**
 * 同时支持 header、cookies方式
 * 
 * @author xiebin
 *
 */
public class CookieAndHeadHttpSessionStrategy extends AbstractService implements MultiHttpSessionStrategy {

	private List<HttpSessionStrategy> delegateList = new ArrayList<>();

	/**
	 * 增加session策略
	 * 
	 * @param httpSessionStrategy
	 */
	public void addHttpSessionStrategy(HttpSessionStrategy httpSessionStrategy) {
		this.delegateList.add(httpSessionStrategy);
	}
	/**
	 * 获取sessionId
	 */
    @Override
	public String getRequestedSessionId(HttpServletRequest request) {
		String sessionId = "";
		for (HttpSessionStrategy delegate : delegateList) {
			sessionId = delegate.getRequestedSessionId(request);
			if (StringUtils.isNotBlank(sessionId)) {
				break;
			}
		}
		info("session get:{}",sessionId);
		return sessionId;
	}
    @Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		for (HttpSessionStrategy delegate : delegateList) {
			delegate.onNewSession(session, request, response);
		}
		info("session set:{}",session.getId());
	}
    @Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		for (HttpSessionStrategy delegate : delegateList) {
			delegate.onInvalidateSession(request, response);
		}
	}
    @Override
	public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
		return request;
	}
    @Override
	public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
		return response;
	}

}
