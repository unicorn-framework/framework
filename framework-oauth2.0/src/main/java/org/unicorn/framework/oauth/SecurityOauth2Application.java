package org.unicorn.framework.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicorn.framework.util.json.GsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@SpringBootApplication
@Slf4j
public class SecurityOauth2Application {


    public static void main(String[] args) {
        SpringApplication.run(SecurityOauth2Application.class, args);
    }

    @GetMapping("/user")
    public Object getCurrentUser1(Authentication authentication, HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("【SecurityOauth2Application】 getCurrentUser1 authenticaiton={}", GsonUtils.toJson(authentication));
        return authentication;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/forbidden")
    public String getForbidden() {
        return "forbidden";
    }

    @GetMapping("/permitAll")
    public String getPermitAll() {
        return "permitAll";
    }
}
