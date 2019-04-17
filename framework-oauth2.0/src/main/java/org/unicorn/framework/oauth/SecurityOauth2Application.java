package org.unicorn.framework.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

@SpringBootApplication
public class SecurityOauth2Application {
    public static void main(String[] args) {
        SpringApplication.run(SecurityOauth2Application.class, args);
    }
}
