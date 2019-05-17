package org.unicorn.framework.transaction.config;

import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.unicorn.framework.transaction.filter.FescarRMRequestFilter;
import org.unicorn.framework.transaction.interceptor.FescarRestInterceptor;

import java.util.Collection;
import java.util.List;

/**
 * fescar相关配置
 *
 * @author xiebin
 */
@Configuration
public class FescarAutoConfiguration {
    public static final String FESCAR_XID = "Fescar_XID";

    /**
     * 全局事务扫描，设置appName和groupName
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner(Environment environment) {
        String applicationName = environment.getProperty("spring.application.name");
        String groupName = environment.getProperty("fescar.group.name");
        if (applicationName == null) {
            return new GlobalTransactionScanner(groupName == null ? "my_test_tx_group" : groupName);
        } else {
            return new GlobalTransactionScanner(applicationName, groupName == null ? "my_test_tx_group" : groupName);
        }
    }

    /**
     * 为请求添加拦截器
     */
    @Bean
    public Object addFescarInterceptor(Collection<RestTemplate> restTemplates) {
        restTemplates.stream().forEach(restTemplate -> {
            List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
            if (interceptors != null) {
                interceptors.add(fescarRestInterceptor());
            }
        });
        return new Object();
    }

    @Bean
    public FescarRMRequestFilter fescarRMRequestFilter() {
        return new FescarRMRequestFilter();
    }

    @Bean
    public FescarRestInterceptor fescarRestInterceptor() {
        return new FescarRestInterceptor();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
