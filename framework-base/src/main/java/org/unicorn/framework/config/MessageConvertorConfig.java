package org.unicorn.framework.config;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.annotation.EnableJacksonCustomized;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
@Configuration
@ConditionalOnBean(annotation=EnableJacksonCustomized.class)
public class MessageConvertorConfig {
	private static final DateFormat DF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Bean
	public ObjectMapper objectMapper(){
		ObjectMapper objectMapper=new ObjectMapper();
		// 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化  
        // Include.Include.ALWAYS 默认  
        // Include.NON_DEFAULT 属性为默认值不序列化  
        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量  
        // Include.NON_NULL 属性为NULL 不序列化  
        objectMapper.setSerializationInclusion(Include.NON_EMPTY);  
		// 忽略json字符串中不识别的属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
		//objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		//时间格式化
		objectMapper.setDateFormat(DF);
		 // 字段保留，将null值转为""  
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){  
        @Override  
        public void serialize(Object o, JsonGenerator jsonGenerator,  
                              SerializerProvider serializerProvider)  
                throws IOException, JsonProcessingException  
        {  
            jsonGenerator.writeString("");  
        }  
    });  
		return objectMapper;
	}
	
	
}
