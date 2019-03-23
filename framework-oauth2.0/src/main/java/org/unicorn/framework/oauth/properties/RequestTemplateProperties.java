package org.unicorn.framework.oauth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author xieibn
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "unicorn.feign.head")
public class RequestTemplateProperties {
    /**
     * 请求模板请求头名称
     */
    private String template = "Authorization";

    /**
     * 请求头名称
     */
    private String request = "Authorization";

}
