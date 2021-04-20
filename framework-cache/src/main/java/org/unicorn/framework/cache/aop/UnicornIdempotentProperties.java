package org.unicorn.framework.cache.aop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiebin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "unicorn.idempotent")
public class UnicornIdempotentProperties {
    /**
     * 幂等的时间窗口:在这个时间窗口内不能重复提交
     * 默认 5秒
     */
    private int timeWindowSeconds = 5;
    /**
     * 业务方需要的友好提示
     * 默认：重复提交
     */
    private String tips = "重复提交";
    /**
     * 当触发重复提交时
     * 全局是否抛出异常
     */
    private boolean globleThrowException = true;


}
