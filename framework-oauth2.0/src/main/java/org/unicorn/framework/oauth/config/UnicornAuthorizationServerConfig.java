//package org.unicorn.framework.oauth.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.unicorn.framework.oauth.properties.OAuth2ClientProperties;
//import org.unicorn.framework.oauth.properties.OAuth2Properties;
//
///**
// * @author xiebin
// * @since 1.0
// */
//@Configuration
//public class UnicornAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private OAuth2Properties oAuth2Properties;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserDetailsService unicornUserDetailsService;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Autowired
//    private WebResponseExceptionTranslator customWebResponseExceptionTranslator;
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.tokenStore(tokenStore)
//                .authenticationManager(authenticationManager)
//                .userDetailsService(unicornUserDetailsService);
//        endpoints.exceptionTranslator(customWebResponseExceptionTranslator);
//    }
//
//    /**
//     * 配置客户端一些信息
//     *
//     * @param clients
//     * @throws Exception
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        InMemoryClientDetailsServiceBuilder build = clients.inMemory();
//        if (oAuth2Properties.getClients()!=null) {
//            for (OAuth2ClientProperties config : oAuth2Properties.getClients()) {
//                build.withClient(config.getClientId())
//                        .secret(config.getClientSecret())
//                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())
//                        .refreshTokenValiditySeconds(60 * 60 * 24 * 15)
//                        .authorizedGrantTypes("refresh_token", "password", "authorization_code")//OAuth2支持的验证模式
//                        .scopes("all");
//            }
//        }
//    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        oauthServer.authenticationEntryPoint(new AuthExceptionEntryPoint());
//    }
//}
