package org.unicorn.framework.mybatis.config.sharding.properties;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.jdbc.sharding")
public class UnicornDataTableRuleProperties {
    /**
     * 配置分表信息     逻辑表=logicName
     *                 逻辑表=dtaNodes
     *                 逻辑表=dtaSourceShardingKey
     *                 逻辑表=tableShardingKey
     */
    private Map<String, UnicornShardingRuleProperties>  tableRule= Maps.newHashMap();


}
