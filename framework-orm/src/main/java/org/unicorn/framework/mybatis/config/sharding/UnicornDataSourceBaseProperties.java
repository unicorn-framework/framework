package org.unicorn.framework.mybatis.config.sharding;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.datasource.base")
public class UnicornDataSourceBaseProperties {
    /**
     *
     */
    private Map<String,Map<String,String>>  master;
    /**
     *
     */
    private Map<String,Map<String,String>>  slave;
}
