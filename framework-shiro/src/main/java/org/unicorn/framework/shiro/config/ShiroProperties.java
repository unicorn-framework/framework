
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
public class ShiroProperties   {
	    /**
	     * 未授权跳转地址
	     */
	    private String unAuthorizedUrl="/unAuthorization";
	    /**
	     * 系统登陆地址
	     */
	    private String loginUrl="/login";
	    /**
	     * 系统登出地址
	     */
	    private String logOutUrl="/logout";
	    
	    /**
	     * 系统登陆成功首页
	     */
	    private String successUrl="http://www.baidu.com";
	    
	    /**
	     * 加密方式 默认加密方式
	     */
	    private String hashAlgorithmName="MD5";
	    
	    /**
	     * hash次数 默认两次
	     */
	    private int hashCount=2;
	    
	    
		 
}

