package org.unicorn.framework.gateway.properties;

import lombok.Data;
import org.assertj.core.util.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author  xiebin
 */
@Data
@Configuration
@ConfigurationProperties(prefix="unicorn.security")
public class UnicornGatewaySecurityProperties {
    /**
     *相差分钟数
     */
    private int diffMinutes=5;

    /**
     *  密钥对   public、private
     *
     */
    private Map<String,String> keyPair;

    /**
     * 是否开启签名检查
     */
    private Boolean signCheckEnable=true;
    /**
     * 是否开启时间检查
     */
    private Boolean timeStampCheckEnable=true;

    /**
     * 是否开启时间检查
     */
    private String appKey;
    /**
     * 忽略的url
     */
    private List<String> ignoreUrls= Lists.newArrayList();
    /**
     * 是否启用安全机制
     */
    private Boolean enable=false;






}
