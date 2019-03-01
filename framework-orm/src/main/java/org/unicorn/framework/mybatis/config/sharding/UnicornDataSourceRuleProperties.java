package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Maps;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author  xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.jdbc.datasource.rule")
public class UnicornDataSourceRuleProperties {
    /**
     * 主从规则配置
     */
    private Map<String,MasterSlaveRuleConfiguration> masterSlaveRule= Maps.newConcurrentMap();
    /**
     * 分片规则配置
     */
    private ShardingRuleConfiguration shardingRule=new ShardingRuleConfiguration();
}
