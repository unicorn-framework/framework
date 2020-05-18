package org.unicorn.framework.api.doc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;

/**
 * @author xiebin
 */
@Configuration
@ConditionalOnProperty(name = {"spring.swagger.enabled", "spring.swagger.validator-plugin"}, havingValue = "true")
public class SwaggerBeanValidatorPluginsConfiguration extends BeanValidatorPluginsConfiguration {
    /**
     * 安全插件,目前不提供使用
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SwaggerSecurityFilterPluginsConfiguration());
        registration.addUrlPatterns("/v2/api-docs", "/swagger-resources");
        return registration;
    }
}
