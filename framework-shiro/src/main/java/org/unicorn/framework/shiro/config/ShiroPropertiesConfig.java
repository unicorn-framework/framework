
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
*@author xiebin
*
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Component  
@ConfigurationProperties(prefix = "unicorn.shiro")   
public class ShiroPropertiesConfig   {
	    /**
	     * 未授权跳转地址
	     */
	    private String unAuthorizedUrl="/unAuthorization";
	    /**
	     * 系统登陆地址
	     */
	    private String loginUrl="/login";
	    
	    /**
	     * 系统登陆成功首页
	     */
	    private String successUrl="http://www.baidu.com";
		 
}

