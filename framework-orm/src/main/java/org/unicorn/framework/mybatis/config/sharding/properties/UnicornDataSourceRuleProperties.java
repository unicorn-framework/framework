package org.unicorn.framework.mybatis.config.sharding.properties;

import com.google.common.collect.Lists;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


/**
 * 1、主从规则
 * 2、分片规则
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
