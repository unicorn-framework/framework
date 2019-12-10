package org.unicorn.framework.base.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 属性处理
 *
 * @author xiebin
 */
@Component
@Slf4j
public class UnicornPropertiesHandler implements PropertySourceLoader {
    @Override
    public String[] getFileExtensions() {
        return new String[]{"properties", "xml", "yaml"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        log.info("加载配置");
        Map<String, ?> properties = loadProperties(resource);
        if (properties.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new OriginTrackedMapPropertySource(name, properties));
    }

    private Map<String, ?> loadProperties(Resource resource) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            inputStream.close();
        } catch (IOException e) {
            log.error("加载配置失败。。。", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭流失败。。。", e);
                }
            }
        }
        return (Map) properties;
    }

}

