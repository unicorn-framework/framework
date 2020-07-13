package org.unicorn.framework.mybatis.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * @author xiebin
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfig implements TransactionManagementConfigurer {

    @Autowired
    @Qualifier("unicornDataSource")
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        try {
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("sqlSessionFactory创建失败");
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 新建一个事务
     *
     * @return
     */
    @Bean
    public UnicornTransactionTemplate newTransactionTemplate() {
        UnicornTransactionTemplate newTransactionTemplate = new UnicornTransactionTemplate();
        newTransactionTemplate.setTransactionManager(annotationDrivenTransactionManager());
        newTransactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        newTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return newTransactionTemplate;
    }

    /**
     * 如果有事务则加入，没有事务则新建事务
     *
     * @return
     */
    @Bean
    public UnicornTransactionTemplate nestedTransactionTemplate() {
        UnicornTransactionTemplate nestedTransactionTemplate = new UnicornTransactionTemplate();
        nestedTransactionTemplate.setTransactionManager(annotationDrivenTransactionManager());
        nestedTransactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        nestedTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        return nestedTransactionTemplate;
    }


}
