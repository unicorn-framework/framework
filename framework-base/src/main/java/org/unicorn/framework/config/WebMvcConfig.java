package org.unicorn.framework.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author xiebin
 *
 */
@Configuration                     
public class WebMvcConfig extends WebMvcConfigurerAdapter  {
	
	private static final String SDF_PARTTERN="yyyy-MM-dd HH:mm:ss";
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		
	MappingJackson2HttpMessageConverter mjmc = new MappingJackson2HttpMessageConverter();
	ObjectMapper objectMapper = new ObjectMapper();
	DeserializationConfig dc = objectMapper.getDeserializationConfig();
	// 设置反序列化日期格式、忽略不存在get、set的属性
	objectMapper.setConfig(dc.with(new SimpleDateFormat(SDF_PARTTERN)).without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
	objectMapper.setDateFormat(new SimpleDateFormat(SDF_PARTTERN));
	mjmc.setObjectMapper(objectMapper);
	// 设置中文编码格式
	List<MediaType> list = new ArrayList<MediaType>();
	list.add(MediaType.APPLICATION_JSON_UTF8);
	mjmc.setSupportedMediaTypes(list);
	converters.add(mjmc);
	}
	
	
}
