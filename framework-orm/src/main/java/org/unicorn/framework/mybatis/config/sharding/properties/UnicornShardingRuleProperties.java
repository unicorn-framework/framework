package org.unicorn.framework.mybatis.config.sharding.properties;

import lombok.Data;

/**
 * @author xiebin
 */
@Data
public class UnicornShardingRuleProperties {
    /**
     *  数据实际节点配置 支持Groovy表达式配置
     *  eg:db0.t_order_0,db0.t_order_1,db1.t_order_0,db1.t_order_1,db2.t_order_0,db2.t_order_1
     *  db${0..2}.t_order_${0..1}
     */
    private String actualDataNodes;
    /**
     * 数据库分片健 列名
     */
    private String dataSourceShardingCloumnName;
    /**
     * 数据库表分健 列名
     */
    private String tableShardingCloumnName;

    /**
     * 数据库分片策略 支持Groovy表达式配置
     */
    private String dataSourceShardingAlgorithmExpression;
    /**
     * 数据库表分片策略 支持Groovy表达式配置
     */
    private String tableShardingAlgorithmExpression;


}
