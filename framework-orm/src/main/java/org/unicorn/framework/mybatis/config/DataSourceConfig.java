package org.unicorn.framework.mybatis.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @author xiebin
 *
 */
@Configuration
public class DataSourceConfig {
	
	@Bean
    public DynamicRoutingDataSource dataSource() {
		DynamicRoutingDataSource routingDataSource=	new DynamicRoutingDataSource();
		// 配置多数据源
        Map<Object, Object> dsMap = new HashMap<Object, Object>(5);
        dsMap.put(RoutingStrategy.Master, busDataSource());
        dsMap.put(RoutingStrategy.Slave, otherDataSource());
        routingDataSource.setDefaultTargetDataSource( busDataSource());
        routingDataSource.setTargetDataSources(dsMap);
        return routingDataSource;
    }

	@Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource busDataSource() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }
	@Bean
    @ConfigurationProperties(prefix="spring.slave.datasource")
    public DataSource otherDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	
}
