package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Maps;
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
     * 配置主库
     */
    private Map<String,Map<String,String>>  master= Maps.newHashMap();
    /**
     *配置从库
     */
    private Map<String,Map<String,String>>  slave= Maps.newHashMap();

    /**
     * 默认数据源名称
     */
    private String defaultDataSourceName;
}
