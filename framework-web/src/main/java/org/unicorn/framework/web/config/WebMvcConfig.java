package org.unicorn.framework.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.unicorn.framework.util.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiebin
 */
@Configuration
//@EnableConfigurationProperties(XssProperties.class)
public class WebMvcConfig implements WebMvcConfigurer {

//    @Autowired
//    private XssProperties xssProperties;
//
//    private static final String SDF_PARTTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mjmc = new MappingJackson2HttpMessageConverter();
        mjmc.setObjectMapper(objectMapper());
        // 设置中文编码格式
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mjmc.setSupportedMediaTypes(list);
        converters.add(mjmc);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JsonUtils.createNewObjectMapper();
    }

}
