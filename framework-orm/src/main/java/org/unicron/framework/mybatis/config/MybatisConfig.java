package org.unicron.framework.mybatis.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
@Configuration
@EnableTransactionManagement
public class MybatisConfig implements  TransactionManagementConfigurer  {
	
	
	   @Autowired
	   private  DataSource dataSource;

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
	    @Bean
	    public UnicornTransactionTemplate newTransactionTemplate() {
	    	UnicornTransactionTemplate newTransactionTemplate=new UnicornTransactionTemplate();
	    	newTransactionTemplate.setTransactionManager(annotationDrivenTransactionManager());
	    	newTransactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
	    	newTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	        return newTransactionTemplate;
	    }
	    
	    @Bean
	    public UnicornTransactionTemplate nestedTransactionTemplate() {
	    	UnicornTransactionTemplate nestedTransactionTemplate=new UnicornTransactionTemplate();
	    	nestedTransactionTemplate.setTransactionManager(annotationDrivenTransactionManager());
	    	nestedTransactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
	    	nestedTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
	        return nestedTransactionTemplate;
	    }
	    
	
}
