package org.unicorn.framework.oss.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * oss存储服务Dto类
 *
 * @author xiebin
 */

@Data
@Configuration
@ConfigurationProperties(prefix="unicorn.oss")
public class OssBucketConfig implements Serializable {

	private static final long serialVersionUID = -1;


	/**
	 * 配置名称
	 */
	private String name;

	/**
	 * bucket名称
	 */
	private String bucketName;

	/**
	 *
	 */
	private String accessKeyId;

	/**
	 *
	 */
	private String accessKeySecret;

	/**
	 *
	 */
	private String endPoint;


	/**
	 * 访问前缀
	 */
	private String accessPrefix;
	/**
	 * 上传文件最大值 默认1024KB
	 */
     private Long maxSizeKb=1l;

}
