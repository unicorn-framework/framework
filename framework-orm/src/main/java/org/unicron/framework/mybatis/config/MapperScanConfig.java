package org.unicron.framework.mybatis.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @author xiebin
 *
 */
@Configuration
@AutoConfigureAfter(MybatisConfig.class)
public class MapperScanConfig   {
		   
	@Bean
    public MapperScannerConfigurer  mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.jiabiango.dao");
        return mapperScannerConfigurer;
    }
}
