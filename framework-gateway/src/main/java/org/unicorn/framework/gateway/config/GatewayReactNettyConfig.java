package org.unicorn.framework.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.unicorn.framework.gateway.properties.UnicornGatewayProperties;

/**
 * 配置gatewat netty IO工作线程数
 * @author xiebin
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(UnicornGatewayProperties.class)
public class GatewayReactNettyConfig {
    @Autowired
    private UnicornGatewayProperties unicornGatewayProperties;

    @Bean
    public ReactorResourceFactory reactorClientResourceFactory() {
        System.setProperty("reactor.netty.ioWorkerCount", unicornGatewayProperties.getWorkerCount());
        return new ReactorResourceFactory();
    }
}
