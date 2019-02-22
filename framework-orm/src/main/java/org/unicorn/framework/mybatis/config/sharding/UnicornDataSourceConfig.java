package org.unicorn.framework.mybatis.config.sharding;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author xiebin
 *
 */
@Slf4j
@Configuration
public class UnicornDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "unicorn.datasource.base")
    public UnicornDataSourceConfigProperties datasourceProperties() {
        UnicornDataSourceConfigProperties properties = new UnicornDataSourceConfigProperties();
        return properties;
    }

    @Bean
    @ConfigurationProperties(prefix = "unicorn.datasource.pool")
    public UnicornDataSourcePoolConfigProperties datasourcePoolProperties() {
        UnicornDataSourcePoolConfigProperties properties = new UnicornDataSourcePoolConfigProperties();
        return properties;
    }


    public UnicornDataSource dataSourceProperties(){
        UnicornDataSource dataSourceProperties=UnicornDataSource.builder().build();
        dataSourceProperties.setMasterDataSource(initMasterDataSource());
        List<UnicornSlaveDataSource> list=initSlaveDataSource();
        Map<String,List<UnicornSlaveDataSource>> map=list.stream().collect(Collectors.groupingBy(UnicornSlaveDataSource::getMasterName));
        dataSourceProperties.setSlaveDataSourcesMap(map);
        return dataSourceProperties;
    }

    /**
     * 初始化主数据源
     * @return
     */
    public UnicornMasterDataSource initMasterDataSource(){
        UnicornMasterDataSource dataSourceProperties=UnicornMasterDataSource.builder().build();
        Map<String,Map<String,String>> masterDataSourceProperties=datasourceProperties().getProperties().get("master");
        Map<String,DataSource> masterDataSourceMap= Maps.newConcurrentMap();
        Set<String> masterNameSet=masterDataSourceProperties.keySet();
        masterNameSet.forEach(masterName->{
            String isDefaultDatasource=masterDataSourceProperties.get(masterName).get("isDefault");
            if("true".equalsIgnoreCase(isDefaultDatasource)){
                dataSourceProperties.setDefaultDataSourceName(masterName);
            }
            masterDataSourceMap.put(masterName, createDataSource(masterDataSourceProperties.get(masterName)));
        });
        dataSourceProperties.setDataSourceMap(masterDataSourceMap);
        return dataSourceProperties;
    }


    /**
     * 初始化从数据源
     * @return
     */
    public List<UnicornSlaveDataSource> initSlaveDataSource(){
        List<UnicornSlaveDataSource> slaveDataSourceList=Lists.newArrayList();
        Map<String,Map<String,String>> slaveDataSourceProperties=datasourceProperties().getProperties().get("slave");
        Set<String> slaveNameSet=slaveDataSourceProperties.keySet();
        slaveNameSet.forEach(slaveName->{
            UnicornSlaveDataSource unicornSlaveDataSource=new UnicornSlaveDataSource();
            String masterName=slaveDataSourceProperties.get(slaveName).get("masterName");
            unicornSlaveDataSource.setMasterName(masterName);
            unicornSlaveDataSource.setDataSource(createDataSource(slaveDataSourceProperties.get(slaveName)));
            slaveDataSourceList.add(unicornSlaveDataSource);
        });
        return slaveDataSourceList;
    }


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
        // 数据源连接池信息绑定
        String datasourcePoolPropertiesName = getProperties(datasourceProperties, "dsType");
        bind(dataSource, datasourcePoolProperties().getProperties().get(datasourcePoolPropertiesName));
        return dataSource;
    }
    /**
     * 获取map属性
     * @param map
     * @param proName
     * @return
     */
    public String getProperties(Map<String, String> map, String proName) {
        return map.get(proName);
    }
    /**
     * 数据源绑定属性
     * @param result
     * @param properties
     */
    public void bind(DataSource result, Map<String, String> properties) {
        MutablePropertyValues mproperties = new MutablePropertyValues(properties);
        new RelaxedDataBinder(result).bind(mproperties);
    }
}
