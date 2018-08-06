package org.unicorn.framework.session.strategy;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.session.Session;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;
import org.unicorn.framework.session.config.SessionPropertiesConfig;
/**
 * token与sessionId之间映射
 * @author xiebin
 *
 */
public class UnicornHeadTokenHttpSessionStrategy implements HttpSessionStrategy {
	// 这用Qualifier注解，如果你的工程还集成了spring-data-redis,需要指定一下用哪一个
	@Qualifier("sessionRedisTemplate")
	@Autowired
	private RedisTemplate redisTemplate;
	private String unicornRedisName = "unicorn:token";
	// 当客户端没有传token参数的时候，避免创建多个无用的session占用redis空间
	private String defaultToken = tokenGenerator();
	private String headerName = "x-auth-token";
	@Autowired
    private SessionPropertiesConfig sessionPropertiesConfig;
	/**
	 * 客户端传过来的是token，需要通过token查找映射关系，拿到sessionid返回
	 */
	public String getRequestedSessionId(HttpServletRequest request) {
		String token = request.getHeader(this.headerName);
		ValueOperations<String, String> vops = redisTemplate.opsForValue();
		if (StringUtils.isBlank(token)) {
			token=defaultToken;
		}
		String sessionid = vops.get(getTokenKey(token));
		if (sessionid != null) {
			redisTemplate.expire(getTokenKey(token), sessionPropertiesConfig.getMaxInactiveIntervalInSeconds(), TimeUnit.SECONDS);
		}
		return sessionid;
	}

	/**
	 * 创建session时，保存xxx和sessionid的映射关系
	 */
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(this.headerName);
		String sessionid = session.getId();
		ValueOperations<String, String> vops = redisTemplate.opsForValue();
		if (StringUtils.isBlank(token)) {
			token = defaultToken;
		}
		vops.set(getTokenKey(token), sessionid);
		redisTemplate.expire(getTokenKey(token), sessionPropertiesConfig.getMaxInactiveIntervalInSeconds(), TimeUnit.SECONDS);
		response.setHeader(this.headerName, token);
	}

	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(this.headerName);
		redisTemplate.expire(getTokenKey(token), 0, TimeUnit.SECONDS);
		this.defaultToken=tokenGenerator();
	}

	/**
	 * The name of the header to obtain the session id from. Default is
	 * "x-auth-token".
	 *
	 * @param headerName
	 *            the name of the header to obtain the session id from.
	 */
	public void setHeaderName(String headerName) {
		Assert.notNull(headerName, "headerName cannot be null");
		this.headerName = headerName;
	}
    /**
     * 可扩展
     * @return
     */
	public String tokenGenerator() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	
	private String getTokenKey(String token){
		return this.unicornRedisName+":"+sessionPropertiesConfig.getNamespace()+":"+token;
	}
}
