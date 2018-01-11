
package org.unicorn.framework.cache.conifg;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
*
*@author xiebin
*
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Component  
@ConfigurationProperties(prefix = "unicorn.session")   
public class SessionPropertiesConfig   {
	    /**
	     * session存活最大时间
	     */
		private Integer maxInactiveIntervalInSeconds;
		/**
		 * session 头部参数名
		 */
		private String headName;
		/**
		 * 头部cookie名
		 */
		private String cookieName;
		/**
		 * session rediskey 存储命名空间
		 */
		private String namespace;
		 
}

