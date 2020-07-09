package org.unicorn.framework.api.doc.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unicorn.framework.api.doc.properties.SwaggerProperties;
import org.unicorn.framework.api.doc.properties.SwaggerSecurityProperties;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.util.*;

import static com.google.common.base.Predicates.*;
import static java.util.stream.Collectors.toList;

/**
 * @author xiebin
 */
@Configuration
@ConditionalOnProperty(name = "spring.swagger.enable", havingValue = "true", matchIfMissing = true)
@Import({Swagger2DocumentationConfiguration.class, SwaggerBeanValidatorPluginsConfiguration.class})
@EnableConfigurationProperties({SwaggerProperties.class, SwaggerSecurityProperties.class})
public class SwaggerAutoConfiguration implements BeanFactoryAware {

    private static final String DEFAULT_GROUP_NAME = "default";
    private static final String BASE_PATH = "/**";

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


//    @Bean
//    @ConditionalOnMissingBean
//    public GlobalAccess globalAccess(SwaggerProperties swaggerProperties) {
//        return new GlobalAccess(swaggerProperties);
//    }

    /**
     * 创建api
     *
     * @param swaggerProperties
     * @return
     */
    @Bean
    public List<Docket> createRestApi(SwaggerProperties swaggerProperties) {
        // 没有分组
        if (swaggerProperties.getGroups().size() == 0) {
            return noGroup(swaggerProperties);
        } else {
            //分组
            return hasGroup(swaggerProperties);
        }
    }

    /**
     * 无分组 创建 docket
     *
     * @param swaggerProperties
     * @param globalAccess
     * @return
     */
    private List<Docket> noGroup(SwaggerProperties swaggerProperties) {
        return createDocketList(DEFAULT_GROUP_NAME, swaggerProperties);
    }

    /**
     * 分组 创建docket
     *
     * @param swaggerProperties
     * @param globalAccess
     * @return
     */
    private List<Docket> hasGroup(SwaggerProperties swaggerProperties) {
        List<Docket> docketList = new LinkedList<>();
        // 分组创建
        for (String groupName : swaggerProperties.getGroups().keySet()) {
            createDocketList(groupName, swaggerProperties);
        }
        docketList.addAll(docketList);
        return docketList;
    }

    /**
     * 创建 docket
     *
     * @param groupName
     * @param swaggerProperties
     * @param excludePath
     * @param basePath
     * @return
     */
    private Docket createDocket(String groupName, SwaggerProperties swaggerProperties, List<String> excludePath, List<String> basePath) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerProperties.getHost())
                .groupName(groupName)
                .apiInfo(apiInfo)
                .globalOperationParameters(buildGlobalOperationParameters(swaggerProperties.getGlobalOperationParameters()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(and(not(or(getExcludePath(excludePath))), or(getBasePath(basePath))))
                .build();
        // 配置全局响应
        if (!CollectionUtils.isEmpty(swaggerProperties.getGlobalResponseMessages())) {
            buildGlobalResponseMessage(swaggerProperties, docket);
        }
        configurableBeanFactory.registerSingleton(groupName, docket);
        return docket;
    }

    /**
     * 获取basePath
     *
     * @param basePaths
     * @return
     */
    List<Predicate<String>> getBasePath(List<String> basePaths) {
        return basePaths.stream().map(PathSelectors::ant).collect(toList());
    }

    /**
     * 获取ExcludePath
     *
     * @param excludePaths
     * @return
     */
    List<Predicate<String>> getExcludePath(List<String> excludePaths) {
        List<Predicate<String>> excludePath = Lists.newArrayList();
        for (String path : excludePaths) {
            excludePath.add(PathSelectors.ant(path));
        }
        return excludePath;
    }

    /**
     * 创建docket list
     *
     * @param groupName
     * @param swaggerProperties
     * @param globalAccess
     * @return
     */
    private List<Docket> createDocketList(String groupName, SwaggerProperties swaggerProperties) {
        List<Docket> docketList = new LinkedList<>();
        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }
        Docket docket = createDocket(groupName, swaggerProperties, swaggerProperties.getExcludePath(), swaggerProperties.getBasePath());
        docketList.add(docket);
        return docketList;
    }


    /**
     * 读取 SwaggerProperties 配置,构建全局响应结果
     *
     * @param swaggerProperties swaggerProperties
     * @param docket            docket 追加对象
     */
    private void buildGlobalResponseMessage(SwaggerProperties swaggerProperties, Docket docket) {
        final Map<RequestMethod, List<SwaggerProperties.ResponseMessageBody>> responseMessages = swaggerProperties.getGlobalResponseMessages();
        final Set<RequestMethod> methods = responseMessages.keySet();
        for (RequestMethod method : methods) {
            final List<SwaggerProperties.ResponseMessageBody> responseMessageBodies = responseMessages.get(method);
            final List<ResponseMessage> messages = responseMessageBodies.stream().map(my -> {
                ResponseMessageBuilder builder = new ResponseMessageBuilder().code(my.getCode()).message(my.getMessage());
                if (!StringUtils.isEmpty(my.getModelRef())) {
                    builder.responseModel(new ModelRef(my.getModelRef()));
                }
                return builder.build();
            }).collect(toList());
            docket.globalResponseMessage(method, messages);
        }
    }


    /**
     * 读取 SwaggerProperties 配置,构建全局操作参数
     *
     * @param globalOperationParameters 全局参数
     * @return 参数集
     */
    private List<Parameter> buildGlobalOperationParameters(List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters) {
        if (Objects.isNull(globalOperationParameters)) {
            return Lists.newArrayList();
        }
        return globalOperationParameters.stream().map(globalOperationParameter -> new ParameterBuilder()
                .name(globalOperationParameter.getName())
                .description(globalOperationParameter.getDescription())
                .modelRef(new ModelRef(globalOperationParameter.getModelRef()))
                .parameterType(globalOperationParameter.getParameterType())
                .required(globalOperationParameter.getRequired())
                .build()).collect(toList());
    }
}