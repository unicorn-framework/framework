package org.unicorn.framework.api.doc.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;

/**
 * @author xiebin
 */
@Configuration
@ConditionalOnProperty(name = {"spring.swagger.enabled", "spring.swagger.validator-plugin"}, havingValue = "true")
public class SwaggerBeanValidatorPluginsConfiguration extends BeanValidatorPluginsConfiguration {

}
