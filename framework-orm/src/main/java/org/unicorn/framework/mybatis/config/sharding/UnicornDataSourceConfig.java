package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Maps;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.core.constant.properties.ShardingPropertiesConstant;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.DataBinder;
import org.unicorn.framework.mybatis.config.sharding.properties.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author xiebin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({UnicornDataSourceBaseProperties.class, UnicornDataSourceRuleProperties.class, UnicornDataSourcePoolProperties.class, UnicornDataTableRuleProperties.class})
public class UnicornDataSourceConfig {
    /**
     * 数据源规则
     */
    @Autowired
    private UnicornDataSourceRuleProperties unicornDataSourceRuleProperties;
    /**
     * 数据源基本属性
     */
    @Autowired
    private UnicornDataSourceBaseProperties unicornDataSourceBaseProperties;
    /**
     * 数据库连接池属性
     */
    @Autowired
    private UnicornDataSourcePoolProperties unicornDataSourcePoolProperties;
    /**
     * 分表规则
     */
    @Autowired
    private UnicornDataTableRuleProperties unicornDataTableRuleProperties;

    @Primary
    @Bean(name = "unicornDataSource")
    public DataSource unicornDataSource() {
        return buildDataSource();
    }

    public DataSource buildDataSource() {
        try {
            //初始化并存储所有的数据源信息
            Map<String, DataSource> dataSourceMap = initDataSourceMap();
            //初始化分片规则配置
            ShardingRuleConfiguration shardingRuleConfiguration = initShardingRuleConfiguration();
            //初始化额外的属性
            Properties pro = iniExtPro();
            //创建并返回分片数据源
            return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, Maps.newConcurrentMap(), pro);
        } catch (Exception e) {
            log.error("创建分片数据源失败", e);
        }
        return null;
    }

    /**
     * 初始化额外的属性
     *
     * @return
     */
    private Properties iniExtPro() {
        Properties pro = new Properties();
        //是否打印分片sql
        pro.put(ShardingPropertiesConstant.SQL_SHOW.getKey(), unicornDataSourceBaseProperties.isShowSql());
        return pro;
    }

    /**
     * 初始化  分片规则配置
     *
     * @return
     */
    private ShardingRuleConfiguration initShardingRuleConfiguration() {
        //获取分片配置   分库|分表
        ShardingRuleConfiguration shardingRuleConfiguration = unicornDataSourceRuleProperties.getShardingRule();

        //获取主从数据源配置
        List<MasterSlaveRuleConfiguration> masterSlaveRuleConfigurationList = unicornDataSourceRuleProperties.getMasterSlaveRule();
        //设置主从规则配置
        shardingRuleConfiguration.setMasterSlaveRuleConfigs(masterSlaveRuleConfigurationList);

        //获取表对应分片配置
        List<TableRuleConfiguration> tableRuleList = new ArrayList<>(shardingRuleConfiguration.getTableRuleConfigs());
        //遍历分表List并设置分库分表策略配置
        tableRuleList.forEach(tableRuleConfiguration -> {
            //获取分片属性
            UnicornShardingRuleProperties unicornShardingRuleProperties = unicornDataTableRuleProperties.getTableRule().get(tableRuleConfiguration.getLogicTable());
            //配置分库策略（Groovy表达式配置db规则）
            tableRuleConfiguration.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration(unicornShardingRuleProperties.getDataSourceShardingCloumnName(), unicornShardingRuleProperties.getDataSourceShardingAlgorithmExpression()));
            // 配置分表策略（Groovy表达式配置表路由规则）
            tableRuleConfiguration.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration(unicornShardingRuleProperties.getTableShardingCloumnName(), unicornShardingRuleProperties.getTableShardingAlgorithmExpression()));
        });
        return shardingRuleConfiguration;
    }

    /**
     * 初始化并存储所有的数据源信息
     *
     * @return
     */
    private Map<String, DataSource> initDataSourceMap() {
        Map<String, DataSource> dataSourceMap = Maps.newHashMap();
        //获取数据源配置 并初始化所有的数据源
        Map<String, Map<String, String>> masterDbMap = unicornDataSourceBaseProperties.getDatasource();
        masterDbMap.keySet().forEach(dataSourceName -> {
            DataSource datasource = createDataSource(masterDbMap.get(dataSourceName));
            dataSourceMap.put(dataSourceName, datasource);
        });
        return dataSourceMap;
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
     * 数据源绑定属性
     *
     * @param result
     * @param properties
     */
    public void bind(DataSource datasource, Map<String, String> properties) {
        //数据源属性
        MutablePropertyValues mproperties = new MutablePropertyValues(properties);
        //将数据源属性绑定
        new DataBinder(datasource).bind(mproperties);
    }
}
