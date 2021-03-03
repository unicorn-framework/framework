package org.unicorn.framework.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.DataBinder;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 构建数据源抽象类
 * @author xiebin
 */
@Slf4j
public abstract class AbstractUnicornDataSourceConfig {


    @Primary
    @Bean(name = "unicornDataSource")
    public DataSource unicornDataSource() {
        return buildDataSource();
    }

    /**
     * 构建数据源
     * @return
     */
    public abstract DataSource buildDataSource();

    /**
     * 根据配置创建数据库对象
     *
     * @param datasourceProperties
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataSource createDataSource(Map<String, String> datasourceProperties,Map<String, String> unicornDataSourcePoolProperties) {
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
            bind(dataSource, unicornDataSourcePoolProperties);
            return dataSource;
        } catch (Exception e) {
            log.error("创建数据源失败", e);
            return null;
        }
    }
    /**
     * 数据源绑定属性
     * @param datasource
     * @param properties
     */
    public void bind(DataSource datasource, Map<String, String> properties) {
        //数据源属性
        MutablePropertyValues mproperties = new MutablePropertyValues(properties);
        //将数据源属性绑定
        new DataBinder(datasource).bind(mproperties);
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
}
