/**
 * Title: MemcachedSessionFilter.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.cache.web;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.utils.DateUtils;
import org.unicorn.framework.base.RequestInfo;
import org.unicorn.framework.base.SpringContextHolder;
import org.unicorn.framework.base.UnicornContext;
import org.unicorn.framework.core.constants.Constants;
import org.unicorn.framework.core.exception.PendingException;
import org.unicorn.framework.util.common.CookieUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Title: MemcachedSessionFilter<br/>
 * Description: <br/>
 * 
 * 
 * @author xiebin
 *
 */
@Slf4j
public class RedisSessionFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = 8716734171872945367L;
	public static final String SESSION_ID = "sessionId";

	private String sessionId;

	public final static String webDomain = ".";// 改成可配置

	private String cookieDomain;

	private String cookiePath = "/";

	private int cookieAge = -1;

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		Cookie[] cookies = request.getCookies();
		Cookie sCookie = null;

		String sid = null;

		// 获取sessionid
		if (StringUtils.isNotBlank(request.getHeader(this.sessionId))) {
			sid = request.getHeader(this.sessionId);
			RequestInfo requestInfo = UnicornContext.getRequestInfo();
			if (requestInfo != null) {
				requestInfo.setRequestTokenId(sid);
			}
			log.debug("get SessionId from HEADER" + sid);// maybe
															// Gid
															// or
															// Sid
			// 以防止GID IOS版本出现的问题
			response.setHeader(this.sessionId, sid);
		} else {
			// 设定通过
			if ((cookies != null) && (cookies.length > 0)) {
				for (int i = 0; i < cookies.length; i++) {
					sCookie = cookies[i];
					log.debug("sCookie.getName()=" + sCookie.getName());
					log.debug("this.sessionId=" + this.sessionId);
					if (sCookie.getName().equalsIgnoreCase(this.sessionId)) {
						log.debug("this.sessionId=" + this.sessionId);
						log.debug("isHttpOnly is " + String.valueOf(!sCookie.isHttpOnly()));
						sid = sCookie.getValue();
						RequestInfo requestInfo =UnicornContext.getRequestInfo();
						if (requestInfo != null) {
							requestInfo.setRequestTokenId(sid);
						}

						// 重新设置超时时间
						sCookie.setMaxAge(this.cookieAge);
						sCookie.setPath(this.cookiePath);
						sCookie.setHttpOnly(true);
						CookieUtil.addCookie(response, sCookie);
					}
				}
			}
			// 表示请求头信息和cookie都没有sid值。
			if (StringUtils.isBlank(sid)) {
				sid = generateSessionId();
				Cookie mycookies = new Cookie(this.sessionId, sid);
				// 只有有域名时才生效
				// if (SystemPropertyConfigure.isPrdEnv()) {
				mycookies.setMaxAge(this.cookieAge);

				// mycookies.setDomain(this.cookieDomain);
				// 应用级别共享
				mycookies.setPath(this.cookiePath);

				// }
				mycookies.setHttpOnly(true);
				CookieUtil.addCookie(response, mycookies);
				RequestInfo requestInfo = UnicornContext.getRequestInfo();;
				if (requestInfo != null) {
					requestInfo.setResponseTokenID(sid);
				}
				response.setHeader(this.sessionId, sid);
				request.setAttribute(this.sessionId, sid);
			}
		}

		SessionManager sessionManager = (SessionManager) SpringContextHolder.getBean(SessionManager.class);
		try {
			sessionManager.initialize(sid);
		} catch (PendingException e) {
			log.warn("initialize session error", e);
		}
		filterChain.doFilter(new HttpServletRequestWrapper(sid, request), servletResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {

		if (StringUtils.isNotBlank(filterConfig.getInitParameter(SESSION_ID))) {
			this.sessionId = filterConfig.getInitParameter(SESSION_ID);
		}
		// 将会话id设置在应用上下文中
		// 将sessionId的名称存储在应用的上下文中
		filterConfig.getServletContext().setAttribute(Constants.SESSION_ID_NAME, this.sessionId);
		this.cookieDomain = filterConfig.getInitParameter("cookieDomain");
		String cookieAge = filterConfig.getInitParameter("cookieAge");
		if (cookieAge == null || !NumberUtils.isNumber(cookieAge)) {
			this.cookieAge = -1;
		} else {
			try {
				this.cookieAge = Integer.valueOf(cookieAge);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (StringUtils.isBlank(this.cookieDomain)) {
			this.cookieDomain = webDomain;
		}

		this.cookiePath = filterConfig.getInitParameter("cookiePath");
		if ((this.cookiePath == null) || (this.cookiePath.length() == 0))
			this.cookiePath = "/";

	}

	public String generateSessionId() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(UUID.randomUUID().toString().replace("-", "")).append("!")
				.append(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
		return strBuilder.toString();

	}
}
