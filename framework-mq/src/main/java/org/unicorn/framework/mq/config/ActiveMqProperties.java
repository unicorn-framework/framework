package org.unicorn.framework.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * @author xiebin
 *
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Component
@ConfigurationProperties(prefix="spring.activemq")
public class ActiveMqProperties {
	/**
	 * brokerUrl地址
	 */
	private String brokerUrl;
	/**
	 * 用户名
	 */
	private String user;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 链接响应超市
	 */
	private  Long connectResposeTimeOut;
}
