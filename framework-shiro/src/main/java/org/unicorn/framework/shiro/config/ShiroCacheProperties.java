
package org.unicorn.framework.shiro.config;

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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "unicorn.shiro.cache")
public class ShiroCacheProperties {
	/**
	 * 是否开启shiro缓存
	 */
	private boolean enable;
	/**
     * 用户权限缓存时间 单位：秒
     */
    private Long authorizationCacheSeconds;

}
