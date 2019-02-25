package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author xiebin
 */
@Data
@ConfigurationProperties(prefix = "unicorn.sharding")
public class UnicornDataTableRuleProperties {
    /**
     * 逻辑表列表
     */
    private List<String> logicTableNames;
    /**
     * 配置分表信息     逻辑表=logicName
     *                 逻辑表=dtaNodes
     *                 逻辑表=dtaSourceShardingKey
     *                 逻辑表=tableShardingKey
     */
    private Map<String, UnicornShardingRuleProperties>  tableRule= Maps.newHashMap();


}
