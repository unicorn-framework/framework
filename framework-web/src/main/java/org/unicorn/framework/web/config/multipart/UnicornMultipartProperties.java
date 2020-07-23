package org.unicorn.framework.web.config.multipart;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * oss存储服务Dto类
 *
 * @author xiebin
 */

@Data
@Configuration
@ConfigurationProperties(prefix="unicorn.multipart")
public class UnicornMultipartProperties implements Serializable {

	private static final long serialVersionUID = -1;

	private boolean enabled = true;
	private String location;
	private String maxFileSize = "1MB";
	private String maxRequestSize = "10MB";
	private int fileSizeThreshold =0;
	private boolean resolveLazily = false;

}
