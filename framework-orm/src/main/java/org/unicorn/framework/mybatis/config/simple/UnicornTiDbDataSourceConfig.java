package org.unicorn.framework.mybatis.config.simple;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.mybatis.config.AbstractUnicornDataSourceConfig;
import org.unicorn.framework.mybatis.config.properties.UnicornDataSourcePoolProperties;
import org.unicorn.framework.mybatis.config.simple.properties.UnicornDataSourceSimpleProperties;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 简单数据源配置类
 *
 * @author xiebin
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "unicorn.datasource.sharding", name = "enable", havingValue = "false", matchIfMissing = true)
@EnableConfigurationProperties({UnicornDataSourceSimpleProperties.class, UnicornDataSourcePoolProperties.class})
public class UnicornTiDbDataSourceConfig extends AbstractUnicornDataSourceConfig {

    /**
     * 数据源基本属性
     */
    @Autowired
    private UnicornDataSourceSimpleProperties unicornDataSourceTiDBProperties;
    /**
     * 数据库连接池属性
     */
    @Autowired
    private UnicornDataSourcePoolProperties unicornDataSourcePoolProperties;


    @Override
    public DataSource buildDataSource() {
        try {
            Map<String, String> datasourceProperties = Convert.convert(Map.class, unicornDataSourceTiDBProperties);
            //初始化并存储所有的数据源信息
            DataSource dataSource = createDataSource(datasourceProperties, unicornDataSourcePoolProperties.getProperties());
            //创建并返回TiDB数据源
            return dataSource;
        } catch (Exception e) {
            log.error("创建数据源失败", e);
        }
        return null;
    }


}
