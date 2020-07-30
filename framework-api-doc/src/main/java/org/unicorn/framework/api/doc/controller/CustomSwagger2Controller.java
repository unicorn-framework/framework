package org.unicorn.framework.api.doc.controller;

import com.google.common.base.Strings;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UrlPathHelper;
import org.unicorn.framework.api.doc.dto.ApiInfoDto;
import org.unicorn.framework.api.doc.properties.SwaggerSecurityProperties;
import org.unicorn.framework.api.doc.service.DocumentationCacheService;
import org.unicorn.framework.api.doc.utils.RequestUtils;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiebin
 */
@RestController
@ApiIgnore
public class CustomSwagger2Controller {

    private static final String SWAGGER_SECURITY_URL = "/v2/swagger-security";
    private static final String SWAGGER_SECURITY_LOGIN_URL = "/v2/swagger-login";
    @Autowired
    private DocumentationCacheService documentationCacheService;
    @Autowired
    private ServiceModelToSwagger2Mapper mapper;
    @Autowired
    private JsonSerializer jsonSerializer;
    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private SwaggerSecurityProperties swaggerSecurityProperties;


    @GetMapping(value = SWAGGER_SECURITY_URL,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, Boolean> getCustomDocumentation() {
        Map<String, Boolean> meteData = new HashMap<>(2);
        if (swaggerSecurityProperties == null) {
            meteData.put("security", true);
        } else {
            meteData.put("security", swaggerSecurityProperties.isFilterPlugin());
        }
        return meteData;
    }

    @PostMapping(value = "/api/search",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Void> apiSearch(@RequestBody ApiInfoDto apiInfoDto, HttpServletRequest servletRequest) {
        //根据group过滤出documentation
        Documentation documentation = documentationCacheService.documentationByGroup(apiInfoDto.getGroupName(), apiInfoDto.getKeyword());
        Swagger swagger = this.mapper.mapDocumentation(documentation);
        UriComponents uriComponents = componentsFrom(servletRequest, swagger.getBasePath());
        swagger.basePath(Strings.isNullOrEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
        if (Strings.isNullOrEmpty(swagger.getHost())) {
            swagger.host(this.hostName(uriComponents));
        }

        return new ResponseEntity(this.jsonSerializer.toJson(swagger), HttpStatus.OK);
    }


    private UriComponents componentsFrom(HttpServletRequest request, String basePath) {
        ServletUriComponentsBuilder builder = fromServletMapping(request, basePath);
        UriComponents components = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request)).build();
        String host = components.getHost();
        if (!StringUtils.hasText(host)) {
            return builder.build();
        } else {
            builder.host(host);
            builder.port(components.getPort());
            return builder.build();
        }
    }

    private static ServletUriComponentsBuilder fromServletMapping(HttpServletRequest request, String basePath) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromContextPath(request);
        builder.replacePath(prependForwardedPrefix(request, basePath));
        if (StringUtils.hasText((new UrlPathHelper()).getPathWithinServletMapping(request))) {
            builder.path(request.getServletPath());
        }

        return builder;
    }

    private static String prependForwardedPrefix(HttpServletRequest request, String path) {
        String prefix = request.getHeader("X-Forwarded-Prefix");
        return prefix != null ? prefix + path : path;
    }


    private String hostName(UriComponents uriComponents) {
        String hostNameOverride = environment.getProperty("springfox.documentation.swagger.v2.host", "DEFAULT");
        if ("DEFAULT".equals(hostNameOverride)) {
            String host = uriComponents.getHost();
            int port = uriComponents.getPort();
            return port > -1 ? String.format("%s:%d", host, port) : host;
        } else {
            return hostNameOverride;
        }
    }


    @PostMapping(value = SWAGGER_SECURITY_LOGIN_URL,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Void> loginSwagger(HttpSession session, HttpServletResponse response, String username, String password) throws IOException {
        if (swaggerSecurityProperties == null) {
            RequestUtils.writeForbidden(response);
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            RequestUtils.writeForbidden(response);
        }
        if (!(username.equals(swaggerSecurityProperties.getUsername()) && password.equals(swaggerSecurityProperties.getPassword()))) {
            RequestUtils.writeForbidden(response);
        }
        final String sessionId = session.getId();
        session.setAttribute(sessionId, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
