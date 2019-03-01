package org.unicorn.framework.mybatis.config.sharding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.jdbc.datasource.pool")
public class UnicornDataSourcePoolProperties {
    /**
     * 数据库连接池配置
     */
    private Map<String,String>  properties;
}
