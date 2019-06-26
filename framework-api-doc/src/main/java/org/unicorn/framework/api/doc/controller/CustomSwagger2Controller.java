package org.unicorn.framework.api.doc.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicorn.framework.api.doc.properties.SwaggerSecurityProperties;
import org.unicorn.framework.api.doc.utils.RequestUtils;
import springfox.documentation.annotations.ApiIgnore;

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
@ConditionalOnProperty(name = "spring.swagger.enabled", havingValue = "true", matchIfMissing = true)
public class CustomSwagger2Controller {

    private static final String SWAGGER_SECURITY_URL = "/v2/swagger-security";
    private static final String SWAGGER_SECURITY_LOGIN_URL = "/v2/swagger-login";
    private final SwaggerSecurityProperties swaggerSecurityProperties;

    public CustomSwagger2Controller(SwaggerSecurityProperties swaggerSecurityProperties) {
        this.swaggerSecurityProperties = swaggerSecurityProperties;
    }

    @GetMapping(value = SWAGGER_SECURITY_URL,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Map<String, Boolean> getCustomDocumentation() {
        Map<String, Boolean> meteData = new HashMap<>(2);
        meteData.put("security", swaggerSecurityProperties.isFilterPlugin());
        return meteData;
    }

    @PostMapping(value = SWAGGER_SECURITY_LOGIN_URL,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Void> loginSwagger(HttpSession session, HttpServletResponse response, String username, String password) throws IOException {
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
