package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Maps;
import io.shardingsphere.core.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.core.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
import io.shardingsphere.core.api.config.TableRuleConfiguration;
import io.shardingsphere.core.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.jdbc.spring.datasource.SpringShardingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author xiebin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({UnicornDataSourceBaseProperties.class, UnicornDataSourcePoolProperties.class, UnicornDataTableRuleProperties.class})
public class UnicornDataSourceConfig {

    @Autowired
    protected UnicornDataSourceBaseProperties unicornShardingDataSourceProperties;

    @Autowired
    protected UnicornDataSourcePoolProperties unicornDataSourcePoolProperties;

    @Autowired
    protected UnicornDataTableRuleProperties unicornDataTableRuleProperties;

    @Primary
    @Bean(name = "unicornDataSource")
    public DataSource unicornDataSource() {
        return buildDataSource();
    }

    public DataSource buildDataSource() {
        Map<String, DataSource> dataSourceMap = shardingDataSourceMap();
        try {
//            DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration(), Maps.newConcurrentMap(), new Properties());
            SpringShardingDataSource dataSource=new SpringShardingDataSource(dataSourceMap,shardingRuleConfiguration(),Maps.newConcurrentMap(), new Properties());
            return dataSource;
        } catch (Exception e) {
            log.error("创建分片数据源失败", e);
        }
        return null;
    }

    @Bean
    public Map<String, DataSource> shardingDataSourceMap() {
        Map<String, DataSource> shardingDataSourceMap = Maps.newHashMap();
        //存储所有的主库信息
        Map<String, Map<String, String>> masterDbMap = unicornShardingDataSourceProperties.getMaster();
        //存储所有的从库信息
        Map<String, Map<String, String>> slaveDbMap = unicornShardingDataSourceProperties.getSlave();
        //获取主库配置 集合大小表示配置了多少个主库
        Set<String> masterKeySet = masterDbMap.keySet();
        //获取从库配置，集合大小表示总共有多少从库
        Set<String> slaveKeySet = slaveDbMap.keySet();
        masterKeySet.forEach(masterDbName -> {
            //用来存储当前主从库数据源
            Map<String, DataSource> masterSlaveMap = Maps.newHashMap();
            //获取 masterDbName对应的数据库配置信息
            Map<String, String> masterDataSourceProperties = masterDbMap.get(masterDbName);
            //创建主库
            DataSource masterDataSource = createDataSource(masterDataSourceProperties);
            //存储主库数据源
            masterSlaveMap.put(masterDbName, masterDataSource);
            //存储当前主库所有从库名
            List<String> slaveDataSourceNameList = Lists.newArrayList();
            //遍历从库
            slaveKeySet.forEach(slaveDbName -> {
                //获取当前出库对应的从库
                if (slaveDbName.startsWith(masterDbName + "_")) {
                    //获取 slaveDbName对应的数据库配置信息
                    Map<String, String> slaveDataSourceProperties = slaveDbMap.get(slaveDbName);
                    DataSource slaveDataSource = createDataSource(slaveDataSourceProperties);
                    if (slaveDataSource != null) {
                        masterSlaveMap.put(slaveDbName, slaveDataSource);
                        slaveDataSourceNameList.add(slaveDbName);
                    }
                }
            });
            //如果当前主库没有配置从库
            if (slaveDataSourceNameList.isEmpty()) {
                shardingDataSourceMap.put(masterDbName, masterDataSource);
            } else {
                //如果当前配置了从库，则构建主从数据源 主从名称、主库名、从库名称
                MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration(masterDbName,masterDbName,slaveDataSourceNameList);
//                //设置主从名称
//                masterSlaveRuleConfiguration.setName(masterDbName);
//                //设置主库名称
//                masterSlaveRuleConfiguration.setMasterDataSourceName(masterDbName);
//                //设置从库名称
//                masterSlaveRuleConfiguration.setSlaveDataSourceNames(slaveDataSourceNameList);
                try {
                    DataSource masterSlaveDataSource = MasterSlaveDataSourceFactory.createDataSource(masterSlaveMap, masterSlaveRuleConfiguration, Maps.newConcurrentMap());
                    shardingDataSourceMap.put(masterDbName, masterSlaveDataSource);
                } catch (Exception e) {
                    log.error("创建主从数据源失败", e);
                }

            }
        });
        return shardingDataSourceMap;
    }

    /**
     * 根据配置创建数据库对象
     *
     * @param datasourceProperties
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataSource createDataSource(Map<String, String> datasourceProperties) {
        try {
            String dataSourceType = getProperties(datasourceProperties, "type");
            Class<? extends DataSource> type = null;
            try {
                type = (Class<? extends DataSource>) Class.forName(dataSourceType);
            } catch (Exception e) {
                log.error("datasource不合法的类型配置", e);
            }
            DataSource dataSource = DataSourceBuilder.create().type(type).build();
            // 数据源基本信息绑定
            bind(dataSource, datasourceProperties);
            //数据源连接池信息绑定
            bind(dataSource, unicornDataSourcePoolProperties.getProperties());
            return dataSource;
        } catch (Exception e) {
            log.error("创建数据源失败", e);
            return null;
        }
    }

    /**
     * 获取map属性
     *
     * @param map
     * @param proName
     * @return
     */
    public String getProperties(Map<String, String> map, String proName) {
        return map.get(proName);
    }

    /**
     * 数据源绑定属性
     *
     * @param result
     * @param properties
     */
    public void bind(DataSource datasource, Map<String, String> properties) {
        //数据源属性
        MutablePropertyValues mproperties = new MutablePropertyValues(properties);
        //将数据源属性绑定
        new RelaxedDataBinder(datasource).bind(mproperties);
    }

    /**
     * 分片规则 分库分表
     *
     * @return ShardingRuleConfiguration
     */
    public ShardingRuleConfiguration shardingRuleConfiguration() {
        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        List<String> logicTableNameList = unicornDataTableRuleProperties.getLogicTableNames();
        logicTableNameList.forEach(logicTableName -> {
            UnicornShardingRuleProperties unicornShardingRuleProperties = unicornDataTableRuleProperties.getTableRule().get(logicTableName);
            TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration();
            //设置逻辑表名 db0.t_order_0,db0.t_order_1,db1.t_order_0,db1.t_order_1,db2.t_order_0,db2.t_order_1
            tableRuleConfig.setLogicTable(logicTableName);
            tableRuleConfig.setKeyGeneratorColumnName("id");
            //设置实际表名及所在数据节点
            tableRuleConfig.setActualDataNodes(unicornShardingRuleProperties.getActualDataNodes());
            // 配置分库策略（Groovy表达式配置db规则）
            tableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration(unicornShardingRuleProperties.getDataSourceShardingCloumnName(), unicornShardingRuleProperties.getDataSourceShardingAlgorithmExpression()));
            // 配置分表策略（Groovy表达式配置表路由规则）
            tableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration(unicornShardingRuleProperties.getTableShardingCloumnName(), unicornShardingRuleProperties.getTableShardingAlgorithmExpression()));
            //将规则添加到分片规则配置中
            shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);
        });


        return shardingRuleConfig;
    }

}
