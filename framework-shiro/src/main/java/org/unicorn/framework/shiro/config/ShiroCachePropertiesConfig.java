
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
public class ShiroCachePropertiesConfig {
	/**
	 * 是否开启自定义缓存
	 */
	private boolean enable = false;

}
