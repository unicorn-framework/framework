package org.unicorn.framework.web.config.multipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传 限制配置
 * @author xiebin
 * @since 1.0
 */
@Configuration
public class MultipartConfig {
    @Autowired
    UnicornMultipartProperties unicornMultipartProperties;
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory=new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(unicornMultipartProperties.getMaxFileSize()));
        factory.setMaxRequestSize(DataSize.parse(unicornMultipartProperties.getMaxRequestSize()));
        factory.setLocation(unicornMultipartProperties.getLocation());
        factory.setFileSizeThreshold(DataSize.ofBytes((long)unicornMultipartProperties.getFileSizeThreshold()));
        return factory.createMultipartConfig();
    }
}
