package org.unicorn.framework.register.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiebin
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ribbon")
public class UnicornRibbonIRuleProperties {


    private String NFLoadBalancerRuleClassName;


}
