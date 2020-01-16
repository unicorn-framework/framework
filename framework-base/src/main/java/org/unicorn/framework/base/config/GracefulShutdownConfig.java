package org.unicorn.framework.base.config;

import io.undertow.UndertowOptions;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.base.fulshutdown.undertow.UnicornUnderTowGracefulShutdownWrapper;

/**
 * @author xiebin
 */
@Configuration
@ConditionalOnClass(GracefulShutdownHandler.class)
public class GracefulShutdownConfig {
    @Autowired
    private UnicornUnderTowGracefulShutdownWrapper gracefulShutdownUndertowWrapper;

    @Bean
    public UndertowServletWebServerFactory servletWebServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownUndertowWrapper));
        factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
        return factory;
    }
}
