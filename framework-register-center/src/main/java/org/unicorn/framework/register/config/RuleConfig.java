package org.unicorn.framework.register.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.unicorn.framework.register.properties.UnicornRibbonIRuleProperties;

/**
 * 兼容全局配置
 * serviceName.ribbon.NFLoadBalancerRuleClassName 具体服务的负载规则
 * ribbon.NFLoadBalancerRuleClassName 全局规则
 *
 * @author xiebin
 */
@Configuration
@EnableConfigurationProperties(UnicornRibbonIRuleProperties.class)
public class RuleConfig {
    @Autowired
    private UnicornRibbonIRuleProperties unicornRibbonIRuleProperties;

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "ribbon", name = "NFLoadBalancerRuleClassName", matchIfMissing = false)
    public IRule ribbonRule() {
        try {
            if (StringUtils.isBlank(unicornRibbonIRuleProperties.getNFLoadBalancerRuleClassName())) {
                return new ZoneAvoidanceRule();
            }
            Class clazz = Class.forName(unicornRibbonIRuleProperties.getNFLoadBalancerRuleClassName());
            return (IRule) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("没有发现NFLoadBalancerRuleClassName对应的类:" + unicornRibbonIRuleProperties.getNFLoadBalancerRuleClassName(), e);
        } catch (Exception e) {
            throw new IllegalStateException("NFLoadBalancerRuleClassName对应的类实例化失败:" + unicornRibbonIRuleProperties.getNFLoadBalancerRuleClassName(), e);
        }
    }
}
