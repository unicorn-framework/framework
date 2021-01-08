package org.unicorn.framework.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.unicorn.framework.oauth.handler.UnicornAccessDeniedHandler;

/**
 * @author xiebin
 */
@Configuration
@EnableAuthorizationServer
@ConditionalOnProperty(prefix = "unicorn.security.oauth2", name = "authorizationServer", havingValue = "true")
public abstract class AbstractUnicornAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private TokenEnhancer unicornTokenEnhancer;

    @Autowired
    private WebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 配置授权以及令牌访问断点和令牌服务
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //token存储
                .tokenStore(tokenStore)
                //异常转换
                .exceptionTranslator(customWebResponseExceptionTranslator)
                //认证管理
                .authenticationManager(authenticationManager)
                //token代理处理
                .tokenEnhancer(unicornTokenEnhancer)
                //用户信息
                .userDetailsService(userDetailsService());
    }

    /**
     * 配置客户端一些信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //客户端详情配置
        clients.withClientDetails(clientDetailsService());
    }

    /**
     * 配置令牌端点的安全约束
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                //认证异常处理
                .authenticationEntryPoint(new UnicornAuthExceptionEntryPoint())
                //访问拒绝处理
                .accessDeniedHandler(new UnicornAccessDeniedHandler())
                //密码处理
                .passwordEncoder(passwordEncoder)
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()");
    }


    /**
     * 客户端明细服务
     *
     * @return
     */
    public abstract ClientDetailsService clientDetailsService();

    /**
     * 用户明细服务
     *
     * @return
     */
    public abstract UserDetailsService userDetailsService();

}
