package org.unicorn.framework.mybatis.config.sharding.properties;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.jdbc.base")
public class UnicornDataSourceBaseProperties {
    /**
     * 配置数据源
     */
    private Map<String,Map<String,String>>  datasource= Maps.newHashMap();


}
