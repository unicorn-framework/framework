package org.unicorn.framework.util.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * jackson 工具类
 *
 * @author xiebin
 */
@Slf4j
public class JsonUtils {

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<List<String>>() {
    };

    public static final TypeReference<Map<String, String>> STRING_MAP_TYPE = new TypeReference<Map<String, String>>() {
    };


    private static ObjectMapper objectMapper = createNewObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ObjectMapper createNewObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 简单类型的设置
         */
        SimpleModule simpleModule = new SimpleModule();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        /**
         * 普通日期序列化&日期格式设置
         */
        DeserializationConfig dc = objectMapper.getDeserializationConfig();
        // 设置反序列化日期格式、忽略不存在get、set的属性
        objectMapper.setConfig(dc.with(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT)).without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT));
        /**
         *  java8 时间序列化设置
         */
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        return objectMapper;
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static String toJsonWithView(Object object, Class<?> serializationView) {
        try {
            return objectMapper.writerWithView(serializationView).writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 是否是有效的json
     *
     * @param content
     * @return
     */
    public static boolean isValidJson(String content) {
        try {
            getObjectMapper().readValue(content, JsonNode.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 反序列化
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T fromJson(String json, TypeReference<T> type)
            throws JsonParseException, JsonMappingException, IOException {
        return (T) objectMapper.readValue(json, type);
    }

    /**
     * 反序列化
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T fromJson(String json, Class<T> cls)
            throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(json, cls);
    }

    /**
     * 容器类
     *
     * @param json
     * @param containCls
     * @param clazz
     * @param <T>
     * @param <S>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T, S> T fromJson(String json, Class<T> containCls, Class<S> clazz) throws JsonParseException, JsonMappingException, IOException {

        JavaType javaType = getCollectionType(containCls, clazz);
        return objectMapper.readValue(json, javaType);
    }


    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 反序列为list
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> List<T> fromJsonList(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = getCollectionType(List.class, cls);
        return fromJson(json, javaType);
    }

    public static <T> T fromJson(String json, Type type) throws JsonParseException, JsonMappingException, IOException {
        return objectMapper.readValue(json,
                objectMapper.getDeserializationConfig().getTypeFactory().constructType(type));
    }

    public static String unprettify(String json) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            return json;
        }
    }

    public static String prettify(String json) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
            return writer.writeValueAsString(node);
        } catch (Exception e) {
            return json;
        }
    }

}
