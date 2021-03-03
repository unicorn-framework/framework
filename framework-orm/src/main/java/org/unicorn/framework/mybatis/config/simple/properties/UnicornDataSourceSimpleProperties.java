package org.unicorn.framework.mybatis.config.simple.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.jdbc.simple.datasource")
public class UnicornDataSourceSimpleProperties {
    /**
     * 配置数据源
     */
    private String type;
    private String url;
    private String username;
    private String password;
    private String driverClassName ;
    /**
     * 是否显示sql
     */
    private boolean showSql=false;
}
