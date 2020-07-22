package org.unicorn.framework.web.undertow;

import io.undertow.Undertow;
import io.undertow.server.ConnectorStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * undertow优雅停机
 * @author  xiebin
 */
@Component
public class UnicornUnderTowGracefulShutdown implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private UnicornUnderTowGracefulShutdownWrapper gracefulShutdownUndertowWrapper;

    @Autowired
    private ServletWebServerApplicationContext context;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        gracefulShutdownUndertowWrapper.getGracefulShutdownHandler().shutdown();
        try {
            UndertowServletWebServer webServer = (UndertowServletWebServer)context.getWebServer();
            Field field = webServer.getClass().getDeclaredField("undertow");
            field.setAccessible(true);
            Undertow undertow = (Undertow) field.get(webServer);
            List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
            Undertow.ListenerInfo listener = listenerInfo.get(0);
            ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
            //检测是否存在活跃的链接  如果有则等待
            while (connectorStatistics.getActiveConnections() > 0){}
        } catch (Exception e) {
            // Application Shutdown
        }
    }
}
