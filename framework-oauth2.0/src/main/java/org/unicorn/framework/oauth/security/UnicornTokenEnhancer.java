package org.unicorn.framework.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.oauth.contans.Contans;
import org.unicorn.framework.oauth.dto.UnicornUser;
import org.unicorn.framework.oauth.properties.OAuth2Properties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author xiebin
 */
@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class UnicornTokenEnhancer implements TokenEnhancer {

    @Autowired
    private OAuth2Properties oAuth2Properties;

    @Autowired
    private CacheService cacheService;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken token = ((DefaultOAuth2AccessToken) accessToken);
            Map<String, Object> additionalInformation = new HashMap<String, Object>();
            additionalInformation.put("clientId", authentication.getOAuth2Request().getClientId());
            if (authentication.getPrincipal() instanceof UnicornUser) {
                UnicornUser user = (UnicornUser) authentication.getPrincipal();
                additionalInformation.put("userInfo", user);
                additionalInformation.put("userId", user.getId());
            }
            //刷新token
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken instanceof DefaultOAuth2RefreshToken) {
                token.setRefreshToken(new DefaultOAuth2RefreshToken(getNewToken()));
            }
            token.setValue(getNewToken());
            token.setAdditionalInformation(additionalInformation);
            cacheService.put(Contans.USER_TOKEN_KEY + "_" + token.getAdditionalInformation().get("userId"), token.getValue(), (token.getExpiresIn() + 10), TimeUnit.SECONDS, Contans.USER_TOKEN_NAMESPACE);
            return token;
        }
        return accessToken;
    }

    /**
     * 生成token的规则
     *
     * @param clientId
     * @param userId
     * @return
     */
    private String getNewToken() {
        return oAuth2Properties.getTokenPrefix() + ":" + UUID.randomUUID().toString().replaceAll("-", "");
    }
}
