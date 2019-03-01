package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
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
    private List<MasterSlaveRuleConfiguration> masterSlaveRule= Lists.newArrayList();
    /**
     * 分片规则配置
     */
    private ShardingRuleConfiguration shardingRule=new ShardingRuleConfiguration();
}
