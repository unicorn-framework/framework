package org.unicorn.framework.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author xiebin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "unicorn.gray")
public class UnicornCoreProperties {

    /**
     * 是否开启灰度
     */
    private Boolean enable = false;


}
