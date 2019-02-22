package org.unicorn.framework.mybatis.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        dsMap.put(RoutingStrategy.Slave, slaveDataSource());
        routingDataSource.setDefaultTargetDataSource( busDataSource());
        routingDataSource.setTargetDataSources(dsMap);
        return routingDataSource;
    }

	@Bean
	//@Primary
    @ConfigurationProperties(prefix="spring.unicorn.datasource")
    public DataSource busDataSource() {
        return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }
	@Bean
    @ConfigurationProperties(prefix="spring.unicorn.slave.datasource")
    public DataSource slaveDataSource() {
		 return DataSourceBuilder.create().type(com.alibaba.druid.pool.DruidDataSource.class).build();
    }
	
	
}
