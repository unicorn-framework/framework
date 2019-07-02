package org.unicorn.framework.oauth.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author xieibn
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "unicorn.security.oauth2")
public class OAuth2Properties {
    /**
     * 默认 jwt签名key
     */
    private String jwtSigningKey = "unicorn";
    /**
     * 客户端配置
     */
    private OAuth2ClientProperties client ;
    /**
     * token前缀
     */
    private String tokenPrefix="unicorn";
    /**
     * 不需要验证的接口  ant风格
     */
    private List<String> permitAlls= Lists.newArrayList("/ver/**", "/login");

    /**
     * 头部名称
     */
    private String headName="Authorization";

 }
