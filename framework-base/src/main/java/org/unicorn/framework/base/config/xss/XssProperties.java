package org.unicorn.framework.base.config.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author xieibn
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "unicorn.xss")
public class XssProperties {


    /**
     * 是否开启xss检查
     */
    private Boolean enable=false;
}
