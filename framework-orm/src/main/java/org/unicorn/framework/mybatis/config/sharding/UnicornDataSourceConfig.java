package org.unicorn.framework.mybatis.config.sharding;

import com.dangdang.ddframe.rdb.sharding.api.MasterSlaveDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.google.common.collect.Maps;
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
import java.util.Set;

/**
 * @author xiebin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({UnicornDataSourceBaseProperties.class, UnicornDataSourcePoolProperties.class})
public class UnicornDataSourceConfig {
    @Autowired
    protected UnicornDataSourceBaseProperties unicornShardingDataSourceProperties;
    @Autowired
    protected UnicornDataSourcePoolProperties unicornDataSourcePoolProperties;

    @Primary
    @Bean(name = "unicornDataSource")
    public DataSource unicornDataSource() {
        return buildDataSource();
    }

    public DataSource buildDataSource() {
        Map<String, DataSource> dataSourceMap = shardingDataSourceMap();
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "ds0");
        DataSource dataSource = dataSourceMap.get("ds0");
        //ShardingDataSourceFactory.createDataSource(shardingRule(dataSourceRule));
        return dataSource;
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
        Set<String> slaveKeySet = slaveDbMap.keySet();
        masterKeySet.forEach(masterDbName -> {
            //获取 masterDbName对应的数据库配置信息
            Map<String, String> masterDataSourceProperties = masterDbMap.get(masterDbName);
            //创建主库
            DataSource masterDataSource = createDataSource(masterDataSourceProperties);
            List<DataSource> slaveDataSourceList = Lists.newArrayList();
            //遍历从库
            slaveKeySet.forEach(slaveDbName -> {
                if (slaveDbName.startsWith(masterDbName + "_")) {
                    //获取 slaveDbName对应的数据库配置信息
                    Map<String, String> slaveDataSourceProperties = slaveDbMap.get(slaveDbName);
                    DataSource slaveDataSource = createDataSource(slaveDataSourceProperties);
                    if (slaveDataSource != null) {
                        slaveDataSourceList.add(slaveDataSource);
                    }
                }
            });
            //如果没有配置从库
            if (slaveDataSourceList.isEmpty()) {
                shardingDataSourceMap.put(masterDbName, masterDataSource);
            } else {
                //如果配置了从库，则构建主从数据源
                DataSource[] slaveDataSourceArr = slaveDataSourceList.stream().toArray(DataSource[]::new);
                DataSource masterSlaveDataSource = MasterSlaveDataSourceFactory.createDataSource(masterDbName, masterDataSource, null, slaveDataSourceArr);
                shardingDataSourceMap.put(masterDbName, masterSlaveDataSource);
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
     * @param dataSourceRule
     * @return
     */
    public ShardingRule shardingRule(DataSourceRule dataSourceRule) {
        return null;
    }

    ;
}
